// Khai báo package com.drinkorder.data.remote cho toàn bộ lớp.
package com.drinkorder.data.remote;

// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;
// Import androidx.lifecycle.MutableLiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.MutableLiveData;

// Import com.google.gson.Gson để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.Gson;
// Import com.google.gson.JsonObject để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.JsonObject;
// Import com.google.gson.JsonParseException để sử dụng các lớp hoặc hàm tương ứng.
import com.google.gson.JsonParseException;

// Import java.util.Set để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Set;
// Import java.util.concurrent.Callable để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Callable;
// Import java.util.concurrent.CopyOnWriteArraySet để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.CopyOnWriteArraySet;
// Import java.util.concurrent.ScheduledExecutorService để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.ScheduledExecutorService;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;
// Import java.util.concurrent.TimeUnit để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.TimeUnit;
// Import java.util.concurrent.atomic.AtomicBoolean để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.atomic.AtomicBoolean;

// Import okhttp3.OkHttpClient để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.OkHttpClient;
// Import okhttp3.Request để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.Request;
// Import okhttp3.Response để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.Response;
// Import okhttp3.WebSocket để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.WebSocket;
// Import okhttp3.WebSocketListener để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.WebSocketListener;
// Import okhttp3.logging.HttpLoggingInterceptor để sử dụng các lớp hoặc hàm tương ứng.
import okhttp3.logging.HttpLoggingInterceptor;

// Định nghĩa lớp ChatSocketClient.
public class ChatSocketClient {
  // Định nghĩa enum ConnectionState.
  public enum ConnectionState { DISCONNECTED, CONNECTING, CONNECTED, FAILED }

  // Định nghĩa interface Listener.
  public interface Listener {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onMessage(JsonObject message);.
    void onMessage(JsonObject message);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onClosed();.
    void onClosed();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onFailure(Throwable t);.
    void onFailure(Throwable t);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final OkHttpClient client.
  private final OkHttpClient client;
  // Khai báo thuộc tính với phạm vi truy cập: private final Gson gson.
  private final Gson gson;
  // Khai báo thuộc tính với phạm vi truy cập: private final String socketUrl.
  private final String socketUrl;
  // Khai báo thuộc tính với phạm vi truy cập: private final Callable<String> tokenProvider.
  private final Callable<String> tokenProvider;
  // Khai báo thuộc tính với phạm vi truy cập: private final MutableLiveData<ConnectionState> connectionState = new MutableLiveData<>(ConnectionState.DISCONNECTED).
  private final MutableLiveData<ConnectionState> connectionState = new MutableLiveData<>(ConnectionState.DISCONNECTED);
  // Khai báo thuộc tính với phạm vi truy cập: private final Set<Listener> listeners = new CopyOnWriteArraySet<>().
  private final Set<Listener> listeners = new CopyOnWriteArraySet<>();
  // Khai báo thuộc tính với phạm vi truy cập: private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor().
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  // Khai báo thuộc tính với phạm vi truy cập: private final AtomicBoolean shouldReconnect = new AtomicBoolean(false).
  private final AtomicBoolean shouldReconnect = new AtomicBoolean(false);
  // Khai báo thuộc tính với phạm vi truy cập: private final AtomicBoolean manualClose = new AtomicBoolean(false).
  private final AtomicBoolean manualClose = new AtomicBoolean(false);

  // Khai báo thuộc tính với phạm vi truy cập: private WebSocket webSocket.
  private WebSocket webSocket;
  // Khai báo thuộc tính với phạm vi truy cập: private int retryAttempt = 0.
  private int retryAttempt = 0;

  // Định nghĩa phương thức ChatSocketClient với phạm vi truy cập tương ứng.
  public ChatSocketClient(String socketUrl, Callable<String> tokenProvider) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: this(socketUrl, tokenProvider, defaultClient(), new Gson());.
    this(socketUrl, tokenProvider, defaultClient(), new Gson());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức ChatSocketClient với phạm vi truy cập tương ứng.
  public ChatSocketClient(String socketUrl, Callable<String> tokenProvider, OkHttpClient client, Gson gson) {
    // Gán giá trị cho biến hoặc thuộc tính: this.socketUrl = socketUrl.
    this.socketUrl = socketUrl;
    // Gán giá trị cho biến hoặc thuộc tính: this.tokenProvider = tokenProvider.
    this.tokenProvider = tokenProvider;
    // Gán giá trị cho biến hoặc thuộc tính: this.client = client.
    this.client = client;
    // Gán giá trị cho biến hoặc thuộc tính: this.gson = gson.
    this.gson = gson;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức defaultClient với phạm vi truy cập tương ứng.
  private static OkHttpClient defaultClient() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: HttpLoggingInterceptor logging = new HttpLoggingInterceptor();.
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: logging.setLevel(HttpLoggingInterceptor.Level.BASIC);.
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
    // Trả về kết quả new OkHttpClient.Builder().
    return new OkHttpClient.Builder()
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .readTimeout(0, TimeUnit.MILLISECONDS).
        .readTimeout(0, TimeUnit.MILLISECONDS)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .pingInterval(30, TimeUnit.SECONDS).
        .pingInterval(30, TimeUnit.SECONDS)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .addInterceptor(logging).
        .addInterceptor(logging)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .build();.
        .build();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức connectionState với phạm vi truy cập tương ứng.
  public LiveData<ConnectionState> connectionState() { return connectionState; }

  // Định nghĩa phương thức addListener với phạm vi truy cập tương ứng.
  public void addListener(Listener listener) { listeners.add(listener); }

  // Định nghĩa phương thức removeListener với phạm vi truy cập tương ứng.
  public void removeListener(Listener listener) { listeners.remove(listener); }

  // Định nghĩa phương thức connect với phạm vi truy cập tương ứng.
  public synchronized void connect() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: manualClose.set(false);.
    manualClose.set(false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: shouldReconnect.set(true);.
    shouldReconnect.set(true);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: scheduleConnect(0);.
    scheduleConnect(0);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức disconnect với phạm vi truy cập tương ứng.
  public synchronized void disconnect() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: shouldReconnect.set(false);.
    shouldReconnect.set(false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: manualClose.set(true);.
    manualClose.set(true);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (webSocket != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: webSocket.close(1000, "client_disconnect");.
      webSocket.close(1000, "client_disconnect");
      // Gán giá trị cho biến hoặc thuộc tính: webSocket = null.
      webSocket = null;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState.postValue(ConnectionState.DISCONNECTED);.
    connectionState.postValue(ConnectionState.DISCONNECTED);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức send với phạm vi truy cập tương ứng.
  public boolean send(Object payload) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (webSocket == null) { return false; }
    // Bắt đầu khối try để bắt lỗi có thể phát sinh.
    try {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String json = payload instanceof String ? (String) payload : gson.toJson(payload);.
      String json = payload instanceof String ? (String) payload : gson.toJson(payload);
      // Trả về kết quả webSocket.send(json);.
      return webSocket.send(json);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    } catch (Exception ex) {
      // Trả về kết quả false;.
      return false;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức scheduleConnect với phạm vi truy cập tương ứng.
  private void scheduleConnect(long delayMs) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: scheduler.schedule(this::openSocket, delayMs, TimeUnit.MILLISECONDS);.
    scheduler.schedule(this::openSocket, delayMs, TimeUnit.MILLISECONDS);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức openSocket với phạm vi truy cập tương ứng.
  private synchronized void openSocket() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!shouldReconnect.get()) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState.postValue(ConnectionState.CONNECTING);.
    connectionState.postValue(ConnectionState.CONNECTING);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Request request = buildRequest();.
    Request request = buildRequest();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: webSocket = client.newWebSocket(request, new WebSocketListenerImpl());.
    webSocket = client.newWebSocket(request, new WebSocketListenerImpl());
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức buildRequest với phạm vi truy cập tương ứng.
  private Request buildRequest() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Request.Builder builder = new Request.Builder().url(socketUrl);.
    Request.Builder builder = new Request.Builder().url(socketUrl);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tokenProvider != null) {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: String token = tokenProvider.call();.
        String token = tokenProvider.call();
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (token != null && !token.isEmpty()) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: builder.addHeader("Authorization", "Bearer " + token);.
          builder.addHeader("Authorization", "Bearer " + token);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (Exception ignored) {
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả builder.build();.
    return builder.build();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp WebSocketListenerImpl kế thừa WebSocketListener.
  private class WebSocketListenerImpl extends WebSocketListener {
    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onOpen với phạm vi truy cập tương ứng.
    public void onOpen(WebSocket webSocket, Response response) {
      // Gán giá trị cho biến hoặc thuộc tính: retryAttempt = 0.
      retryAttempt = 0;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState.postValue(ConnectionState.CONNECTED);.
      connectionState.postValue(ConnectionState.CONNECTED);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onMessage với phạm vi truy cập tương ứng.
    public void onMessage(WebSocket webSocket, String text) {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: JsonObject obj = gson.fromJson(text, JsonObject.class);.
        JsonObject obj = gson.fromJson(text, JsonObject.class);
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (Listener l : listeners) { l.onMessage(obj); }
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (JsonParseException ex) {
        // Bắt đầu vòng lặp for để duyệt dữ liệu.
        for (Listener l : listeners) { l.onFailure(ex); }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onClosed với phạm vi truy cập tương ứng.
    public void onClosed(WebSocket webSocket, int code, String reason) {
      // Gán giá trị cho biến hoặc thuộc tính: ChatSocketClient.this.webSocket = null.
      ChatSocketClient.this.webSocket = null;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState.postValue(ConnectionState.DISCONNECTED);.
      connectionState.postValue(ConnectionState.DISCONNECTED);
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (Listener l : listeners) { l.onClosed(); }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (shouldReconnect.get() && !manualClose.get()) {
        // Gán giá trị cho biến hoặc thuộc tính: retryAttempt = 0.
        retryAttempt = 0;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: scheduleConnect(2000);.
        scheduleConnect(2000);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onFailure với phạm vi truy cập tương ứng.
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
      // Gán giá trị cho biến hoặc thuộc tính: ChatSocketClient.this.webSocket = null.
      ChatSocketClient.this.webSocket = null;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: connectionState.postValue(ConnectionState.FAILED);.
      connectionState.postValue(ConnectionState.FAILED);
      // Bắt đầu vòng lặp for để duyệt dữ liệu.
      for (Listener l : listeners) { l.onFailure(t); }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (shouldReconnect.get()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: long delay = (long) Math.min(30000, Math.pow(2, Math.min(retryAttempt, 5)) * 1000L);.
        long delay = (long) Math.min(30000, Math.pow(2, Math.min(retryAttempt, 5)) * 1000L);
        // Thay đổi giá trị biến thông qua toán tử tăng/giảm.
        retryAttempt++;
        // Thực hiện lời gọi phương thức hoặc khởi tạo: scheduleConnect(delay);.
        scheduleConnect(delay);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
