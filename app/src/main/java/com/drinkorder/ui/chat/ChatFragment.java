package com.drinkorder.ui.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
import com.drinkorder.data.remote.ChatSocketClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ChatFragment extends Fragment {

  private ChatViewModel viewModel;
  private ChatMessagesAdapter adapter;
  private RecyclerView rvMessages;
  private TextView tvConnectionStatus;
  private TextView tvThreadTitle;
  private TextView tvThreadSubtitle;
  private TextView tvEmptyState;
  private TextInputEditText edtMessage;
  private ImageButton btnSend;
  private boolean hasActiveThread = false;

  private final TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      updateSendButtonState();
    }

    @Override
    public void afterTextChanged(Editable s) { }
  };

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat, container, false);
    rvMessages = view.findViewById(R.id.rvMessages);
    tvConnectionStatus = view.findViewById(R.id.tvConnectionStatus);
    tvThreadTitle = view.findViewById(R.id.tvThreadTitle);
    tvThreadSubtitle = view.findViewById(R.id.tvThreadSubtitle);
    tvEmptyState = view.findViewById(R.id.tvEmptyState);
    edtMessage = view.findViewById(R.id.edtMessage);
    btnSend = view.findViewById(R.id.btnSend);

    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    layoutManager.setStackFromEnd(true);
    rvMessages.setLayoutManager(layoutManager);
    adapter = new ChatMessagesAdapter();
    rvMessages.setAdapter(adapter);

    btnSend.setOnClickListener(v -> {
      if (viewModel != null) {
        CharSequence text = edtMessage.getText();
        viewModel.sendMessage(text != null ? text.toString() : null);
        edtMessage.setText("");
      }
    });

    edtMessage.addTextChangedListener(textWatcher);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);
    viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);
    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
    viewModel.getThreads().observe(getViewLifecycleOwner(), threads -> {
      if (threads == null || threads.isEmpty()) {
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText(R.string.chat_empty_state);
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
    if (viewModel != null) {
      viewModel.connect();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (viewModel != null) {
      viewModel.disconnect();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    edtMessage.removeTextChangedListener(textWatcher);
    rvMessages.setAdapter(null);
    adapter = null;
  }

  private void renderConnectionState(ChatSocketClient.ConnectionState state) {
    if (tvConnectionStatus == null) return;
    int textRes;
    if (state == ChatSocketClient.ConnectionState.CONNECTED) {
      textRes = R.string.chat_status_connected;
    } else if (state == ChatSocketClient.ConnectionState.CONNECTING) {
      textRes = R.string.chat_status_connecting;
    } else if (state == ChatSocketClient.ConnectionState.FAILED) {
      textRes = R.string.chat_status_failed;
    } else {
      textRes = R.string.chat_status_disconnected;
    }
    tvConnectionStatus.setText(textRes);
  }

  private void renderActiveThread(@Nullable ChatThreadEntity thread) {
    hasActiveThread = thread != null;
    if (thread == null) {
      tvThreadTitle.setText(R.string.chat_default_title);
      tvThreadSubtitle.setText(R.string.chat_default_subtitle);
    } else {
      if (TextUtils.isEmpty(thread.title)) {
        tvThreadTitle.setText(R.string.chat_default_title);
      } else {
        tvThreadTitle.setText(thread.title);
      }
      String partnerName = TextUtils.isEmpty(thread.title)
          ? getString(R.string.chat_default_title)
          : thread.title;
      tvThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));
    }
    updateSendButtonState();
  }

  private void renderMessages(@Nullable List<ChatMessageEntity> messages) {
    if (adapter == null) return;
    adapter.submitList(messages);
    if (messages == null || messages.isEmpty()) {
      tvEmptyState.setVisibility(View.VISIBLE);
      tvEmptyState.setText(R.string.chat_empty_state);
    } else {
      tvEmptyState.setVisibility(View.GONE);
      rvMessages.post(() -> {
        if (adapter != null && adapter.getItemCount() > 0) {
          rvMessages.scrollToPosition(adapter.getItemCount() - 1);
        }
      });
      if (viewModel != null) {
        viewModel.markCurrentThreadRead();
      }
    }
  }

  private void updateSendButtonState() {
    if (btnSend == null) return;
    CharSequence text = edtMessage != null ? edtMessage.getText() : null;
    boolean hasContent = text != null && text.length() > 0;
    btnSend.setEnabled(hasActiveThread && hasContent);
  }
}
