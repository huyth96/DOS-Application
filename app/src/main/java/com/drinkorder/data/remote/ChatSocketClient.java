package com.drinkorder.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;

public class ChatSocketClient {
  public enum ConnectionState { DISCONNECTED, CONNECTING, CONNECTED, FAILED }

  public interface Listener {
    void onMessage(JsonObject message);
    void onClosed();
    void onFailure(Throwable t);
  }

  private final OkHttpClient client;
  private final Gson gson;
  private final String socketUrl;
  private final Callable<String> tokenProvider;
  private final MutableLiveData<ConnectionState> connectionState = new MutableLiveData<>(ConnectionState.DISCONNECTED);
  private final Set<Listener> listeners = new CopyOnWriteArraySet<>();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final AtomicBoolean shouldReconnect = new AtomicBoolean(false);
  private final AtomicBoolean manualClose = new AtomicBoolean(false);

  private WebSocket webSocket;
  private int retryAttempt = 0;

  public ChatSocketClient(String socketUrl, Callable<String> tokenProvider) {
    this(socketUrl, tokenProvider, defaultClient(), new Gson());
  }

  public ChatSocketClient(String socketUrl, Callable<String> tokenProvider, OkHttpClient client, Gson gson) {
    this.socketUrl = socketUrl;
    this.tokenProvider = tokenProvider;
    this.client = client;
    this.gson = gson;
  }

  private static OkHttpClient defaultClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build();
  }

  public LiveData<ConnectionState> connectionState() { return connectionState; }

  public void addListener(Listener listener) { listeners.add(listener); }

  public void removeListener(Listener listener) { listeners.remove(listener); }

  public synchronized void connect() {
    manualClose.set(false);
    shouldReconnect.set(true);
    scheduleConnect(0);
  }

  public synchronized void disconnect() {
    shouldReconnect.set(false);
    manualClose.set(true);
    if (webSocket != null) {
      webSocket.close(1000, "client_disconnect");
      webSocket = null;
    }
    connectionState.postValue(ConnectionState.DISCONNECTED);
  }

  public boolean send(Object payload) {
    if (webSocket == null) { return false; }
    try {
      String json = payload instanceof String ? (String) payload : gson.toJson(payload);
      return webSocket.send(json);
    } catch (Exception ex) {
      return false;
    }
  }

  private void scheduleConnect(long delayMs) {
    scheduler.schedule(this::openSocket, delayMs, TimeUnit.MILLISECONDS);
  }

  private synchronized void openSocket() {
    if (!shouldReconnect.get()) { return; }
    connectionState.postValue(ConnectionState.CONNECTING);
    Request request = buildRequest();
    webSocket = client.newWebSocket(request, new WebSocketListenerImpl());
  }

  private Request buildRequest() {
    Request.Builder builder = new Request.Builder().url(socketUrl);
    if (tokenProvider != null) {
      try {
        String token = tokenProvider.call();
        if (token != null && !token.isEmpty()) {
          builder.addHeader("Authorization", "Bearer " + token);
        }
      } catch (Exception ignored) {
      }
    }
    return builder.build();
  }

  private class WebSocketListenerImpl extends WebSocketListener {
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
      retryAttempt = 0;
      connectionState.postValue(ConnectionState.CONNECTED);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
      try {
        JsonObject obj = gson.fromJson(text, JsonObject.class);
        for (Listener l : listeners) { l.onMessage(obj); }
      } catch (JsonParseException ex) {
        for (Listener l : listeners) { l.onFailure(ex); }
      }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
      ChatSocketClient.this.webSocket = null;
      connectionState.postValue(ConnectionState.DISCONNECTED);
      for (Listener l : listeners) { l.onClosed(); }
      if (shouldReconnect.get() && !manualClose.get()) {
        retryAttempt = 0;
        scheduleConnect(2000);
      }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
      ChatSocketClient.this.webSocket = null;
      connectionState.postValue(ConnectionState.FAILED);
      for (Listener l : listeners) { l.onFailure(t); }
      if (shouldReconnect.get()) {
        long delay = (long) Math.min(30000, Math.pow(2, Math.min(retryAttempt, 5)) * 1000L);
        retryAttempt++;
        scheduleConnect(delay);
      }
    }
  }
}
