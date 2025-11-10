package com.drinkorder.ui.admin;

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
import com.drinkorder.ui.chat.ChatMessagesAdapter;
import com.drinkorder.ui.chat.ChatThreadsAdapter;
import com.drinkorder.ui.chat.ChatViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdminChatFragment extends Fragment {

  private ChatViewModel viewModel;
  private ChatThreadsAdapter threadsAdapter;
  private ChatMessagesAdapter messagesAdapter;
  private RecyclerView rvThreads;
  private RecyclerView rvMessages;
  private TextView tvConnectionStatus;
  private TextView tvSelectedThreadTitle;
  private TextView tvSelectedThreadSubtitle;
  private TextView tvEmptyState;
  private TextInputEditText edtMessage;
  private ImageButton btnSend;
  private boolean hasActiveThread = false;
  private boolean hasAnyThread = false;

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
    View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);
    rvThreads = view.findViewById(R.id.rvThreads);
    rvMessages = view.findViewById(R.id.rvAdminMessages);
    tvConnectionStatus = view.findViewById(R.id.tvAdminConnectionStatus);
    tvSelectedThreadTitle = view.findViewById(R.id.tvSelectedThreadTitle);
    tvSelectedThreadSubtitle = view.findViewById(R.id.tvSelectedThreadSubtitle);
    tvEmptyState = view.findViewById(R.id.tvAdminEmptyState);
    edtMessage = view.findViewById(R.id.edtAdminMessage);
    btnSend = view.findViewById(R.id.btnAdminSend);

    rvThreads.setLayoutManager(new LinearLayoutManager(requireContext()));
    threadsAdapter = new ChatThreadsAdapter(thread -> {
      if (viewModel != null) {
        viewModel.selectThread(thread.threadId);
      }
    });
    rvThreads.setAdapter(threadsAdapter);

    LinearLayoutManager messagesLayoutManager = new LinearLayoutManager(requireContext());
    messagesLayoutManager.setStackFromEnd(true);
    rvMessages.setLayoutManager(messagesLayoutManager);
    messagesAdapter = new ChatMessagesAdapter();
    rvMessages.setAdapter(messagesAdapter);

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
    messagesAdapter.setLocalDisplayName(viewModel.getLocalDisplayName());
    threadsAdapter.setLocalRole(viewModel.getSenderRole());

    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);
    viewModel.getThreads().observe(getViewLifecycleOwner(), this::renderThreads);
    viewModel.getActiveThreadId().observe(getViewLifecycleOwner(), id -> {
      threadsAdapter.setSelectedThreadId(id);
      hasActiveThread = !TextUtils.isEmpty(id);
      updateSendButtonState();
    });
    viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);
    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
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
    rvThreads.setAdapter(null);
    rvMessages.setAdapter(null);
    threadsAdapter = null;
    messagesAdapter = null;
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

  private void renderThreads(@Nullable List<ChatThreadEntity> threads) {
    if (threadsAdapter != null) {
      if (threads == null || threads.isEmpty()) {
        threadsAdapter.submitList(java.util.Collections.emptyList());
      } else {
        threadsAdapter.submitList(new java.util.ArrayList<>(threads));
      }
    }
    hasAnyThread = threads != null && !threads.isEmpty();
    if (!hasAnyThread) {
      if (threadsAdapter != null) {
        threadsAdapter.setSelectedThreadId(null);
      }
      tvEmptyState.setVisibility(View.VISIBLE);
      tvEmptyState.setText(R.string.chat_no_threads);
      tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);
      hasActiveThread = false;
    } else {
      tvEmptyState.setVisibility(View.GONE);
    }
    updateSendButtonState();
  }

  private void renderActiveThread(@Nullable ChatThreadEntity thread) {
    hasActiveThread = thread != null;
    if (thread == null) {
      tvSelectedThreadTitle.setText(R.string.chat_default_title);
      tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);
      if (messagesAdapter != null) {
        messagesAdapter.setRemoteDisplayName(getString(R.string.chat_default_title));
      }
    } else {
      if (TextUtils.isEmpty(thread.title)) {
        tvSelectedThreadTitle.setText(R.string.chat_default_title);
      } else {
        tvSelectedThreadTitle.setText(thread.title);
      }
      String partnerName = TextUtils.isEmpty(thread.title)
          ? getString(R.string.chat_default_title)
          : thread.title;
      tvSelectedThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));
      if (messagesAdapter != null) {
        messagesAdapter.setRemoteDisplayName(partnerName);
      }
    }
    updateSendButtonState();
  }

  private void renderMessages(@Nullable List<ChatMessageEntity> messages) {
    if (messagesAdapter == null) return;
    messagesAdapter.submitSafeList(messages);
    if (messages == null || messages.isEmpty()) {
      if (!hasAnyThread) {
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText(R.string.chat_no_threads);
      } else if (!hasActiveThread) {
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText(R.string.chat_select_thread);
      } else {
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText(R.string.chat_empty_state);
      }
    } else {
      tvEmptyState.setVisibility(View.GONE);
      rvMessages.post(() -> {
        if (messagesAdapter != null && messagesAdapter.getItemCount() > 0) {
          rvMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
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
