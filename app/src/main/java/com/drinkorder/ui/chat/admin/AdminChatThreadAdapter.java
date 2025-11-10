package com.drinkorder.ui.chat.admin;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.model.ChatMessage;
import com.drinkorder.data.model.ChatThreadSummary;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdminChatThreadAdapter extends RecyclerView.Adapter<AdminChatThreadAdapter.ThreadHolder> {

  public interface OnThreadClickListener {
    void onThreadClick(@NonNull ChatThreadSummary summary);
  }

  private final List<ChatThreadSummary> items = new ArrayList<>();
  private final OnThreadClickListener listener;
  private String selectedThreadId;

  public AdminChatThreadAdapter(OnThreadClickListener listener) {
    this.listener = listener;
  }

  public void submit(List<ChatThreadSummary> list) {
    items.clear();
    if (list != null) items.addAll(list);
    notifyDataSetChanged();
  }

  public void setSelectedThreadId(String threadId) {
    selectedThreadId = threadId;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ThreadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_admin_chat_thread, parent, false);
    return new ThreadHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ThreadHolder holder, int position) {
    ChatThreadSummary summary = items.get(position);
    holder.bind(summary, summary.getThreadId().equals(selectedThreadId));
    holder.itemView.setOnClickListener(v -> {
      if (listener != null) listener.onThreadClick(summary);
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class ThreadHolder extends RecyclerView.ViewHolder {

    private final MaterialCardView cardView;
    private final View presenceView;
    private final TextView tvName;
    private final TextView tvUsername;
    private final TextView tvSnippet;
    private final TextView tvTime;
    private final TextView tvUnread;

    ThreadHolder(@NonNull View itemView) {
      super(itemView);
      cardView = (MaterialCardView) itemView;
      presenceView = itemView.findViewById(R.id.viewPresence);
      tvName = itemView.findViewById(R.id.tvThreadName);
      tvUsername = itemView.findViewById(R.id.tvThreadUsername);
      tvSnippet = itemView.findViewById(R.id.tvThreadSnippet);
      tvTime = itemView.findViewById(R.id.tvThreadTime);
      tvUnread = itemView.findViewById(R.id.tvThreadUnread);
    }

    void bind(ChatThreadSummary summary, boolean selected) {
      tvName.setText(summary.getDisplayName());
      tvUsername.setText(summary.getUsername().isEmpty() ? "" : "@" + summary.getUsername());
      String snippet = summary.getLastMessage();
      if (snippet == null || snippet.isEmpty()) {
        snippet = itemView.getContext().getString(R.string.admin_chat_empty_message);
      }
      if (summary.getLastSenderRole() == ChatMessage.SenderRole.ADMIN) {
        snippet = itemView.getContext().getString(R.string.admin_chat_snippet_admin_prefix, snippet);
      }
      tvSnippet.setText(snippet);
      long time = summary.getLastMessageAt();
      if (time <= 0) {
        tvTime.setText("--");
      } else {
        CharSequence span = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        tvTime.setText(span);
      }
      if (summary.getUnreadCount() > 0) {
        tvUnread.setVisibility(View.VISIBLE);
        tvUnread.setText(String.valueOf(summary.getUnreadCount()));
      } else {
        tvUnread.setVisibility(View.GONE);
      }
      presenceView.setBackgroundResource(summary.isOnline() ? R.drawable.bg_chat_presence_online : R.drawable.bg_chat_presence_offline);
      int strokeColor = ContextCompat.getColor(itemView.getContext(),
          selected ? R.color.brand_primary : R.color.brand_outline);
      cardView.setStrokeColor(strokeColor);
      int background = ContextCompat.getColor(itemView.getContext(),
          selected ? R.color.brand_primary_container : R.color.brand_surface_elevated);
      cardView.setCardBackgroundColor(background);
    }
  }
}
