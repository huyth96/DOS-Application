package com.drinkorder.ui.chat.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.model.ChatMessage;
import com.drinkorder.data.model.ChatThreadSummary;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdminChatDetailFragment extends Fragment {

  private static final String ARG_THREAD_ID = "thread_id";

  public static AdminChatDetailFragment newInstance(@NonNull String threadId) {
    AdminChatDetailFragment fragment = new AdminChatDetailFragment();
    Bundle args = new Bundle();
    args.putString(ARG_THREAD_ID, threadId);
    fragment.setArguments(args);
    return fragment;
  }

  private String threadId;
  private AdminChatDetailViewModel viewModel;
  private AdminChatMessageAdapter adapter;
  private RecyclerView recyclerView;
  private TextInputEditText edtMessage;
  private MaterialButton btnSend;
  private TextView tvName;
  private TextView tvPresence;
  private TextView tvConnection;
  private boolean socketOnline = true;

  public String getThreadId() {
    return threadId;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      threadId = args.getString(ARG_THREAD_ID);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_admin_chat_detail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = view.findViewById(R.id.rvChatMessages);
    edtMessage = view.findViewById(R.id.edtChatMessage);
    btnSend = view.findViewById(R.id.btnSendMessage);
    tvName = view.findViewById(R.id.tvChatDetailName);
    tvPresence = view.findViewById(R.id.tvChatDetailPresence);
    tvConnection = view.findViewById(R.id.tvChatDetailConnection);

    adapter = new AdminChatMessageAdapter();
    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    recyclerView.setAdapter(adapter);

    viewModel = new ViewModelProvider(this).get(AdminChatDetailViewModel.class);
    if (threadId != null) {
      viewModel.init(threadId);
    }

    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
    viewModel.getThreadSummary().observe(getViewLifecycleOwner(), this::renderThreadSummary);
    viewModel.observeConnectionState().observe(getViewLifecycleOwner(), connected -> {
      socketOnline = connected == null || connected;
      updateConnectionState();
    });

    btnSend.setOnClickListener(v -> {
      if (viewModel == null) return;
      String message = edtMessage.getText() == null ? "" : edtMessage.getText().toString().trim();
      if (message.isEmpty()) return;
      viewModel.sendMessage(message);
      edtMessage.setText("");
    });

    if (edtMessage != null) {
      edtMessage.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          updateSendEnabled();
        }
        @Override public void afterTextChanged(Editable s) {}
      });
    }
    updateSendEnabled();
  }

  private void renderMessages(List<ChatMessage> messages) {
    adapter.submit(messages);
    if (messages != null && !messages.isEmpty()) {
      recyclerView.post(() -> recyclerView.scrollToPosition(adapter.getItemCount() - 1));
    }
  }

  private void renderThreadSummary(ChatThreadSummary summary) {
    if (summary == null) {
      tvName.setText(R.string.admin_chat_unknown_user);
      tvPresence.setText("");
      return;
    }
    tvName.setText(summary.getDisplayName());
    int presenceRes = summary.isOnline() ? R.string.admin_chat_presence_online : R.string.admin_chat_presence_offline;
    tvPresence.setText(getString(presenceRes));
  }

  private void updateConnectionState() {
    if (tvConnection != null) {
      tvConnection.setVisibility(socketOnline ? View.GONE : View.VISIBLE);
    }
    updateSendEnabled();
  }

  private void updateSendEnabled() {
    if (btnSend == null || edtMessage == null) return;
    String text = edtMessage.getText() == null ? "" : edtMessage.getText().toString().trim();
    btnSend.setEnabled(socketOnline && !text.isEmpty());
  }
}
