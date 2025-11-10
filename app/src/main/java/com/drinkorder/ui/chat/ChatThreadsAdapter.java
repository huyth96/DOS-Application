package com.drinkorder.ui.chat;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

import java.util.Locale;

public class ChatThreadsAdapter extends ListAdapter<ChatThreadEntity, ChatThreadsAdapter.ThreadViewHolder> {

  public interface Listener {
    void onThreadSelected(@NonNull ChatThreadEntity thread);
  }

  private final Listener listener;
  private String selectedThreadId;

  public ChatThreadsAdapter(@NonNull Listener listener) {
    super(DIFF_CALLBACK);
    this.listener = listener;
  }

  private static final DiffUtil.ItemCallback<ChatThreadEntity> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatThreadEntity oldItem, @NonNull ChatThreadEntity newItem) {
          return TextUtils.equals(oldItem.threadId, newItem.threadId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatThreadEntity oldItem, @NonNull ChatThreadEntity newItem) {
          return TextUtils.equals(oldItem.title, newItem.title)
              && TextUtils.equals(oldItem.lastMessage, newItem.lastMessage)
              && TextUtils.equals(oldItem.lastSenderRole, newItem.lastSenderRole)
              && oldItem.lastTimestamp == newItem.lastTimestamp
              && oldItem.unreadCount == newItem.unreadCount;
        }
      };

  public void setSelectedThreadId(String threadId) {
    selectedThreadId = threadId;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_thread, parent, false);
    return new ThreadViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
    ChatThreadEntity item = getItem(position);
    if (item == null) return;
    holder.bind(item, TextUtils.equals(item.threadId, selectedThreadId));
    holder.itemView.setOnClickListener(v -> listener.onThreadSelected(item));
  }

  static class ThreadViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvTitle;
    private final TextView tvPreview;
    private final TextView tvTimestamp;
    private final TextView tvUnread;

    ThreadViewHolder(@NonNull View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tvThreadTitle);
      tvPreview = itemView.findViewById(R.id.tvThreadPreview);
      tvTimestamp = itemView.findViewById(R.id.tvThreadTimestamp);
      tvUnread = itemView.findViewById(R.id.tvUnreadBadge);
    }

    void bind(@NonNull ChatThreadEntity entity, boolean selected) {
      String title = entity.title;
      if (TextUtils.isEmpty(title)) {
        title = itemView.getContext().getString(R.string.chat_default_title);
      }
      tvTitle.setText(title);

      String preview = entity.lastMessage;
      if (TextUtils.isEmpty(preview)) {
        preview = itemView.getContext().getString(R.string.chat_default_subtitle);
      }
      tvPreview.setText(preview);

      CharSequence time = DateUtils.getRelativeTimeSpanString(
          entity.lastTimestamp,
          System.currentTimeMillis(),
          DateUtils.MINUTE_IN_MILLIS,
          DateUtils.FORMAT_ABBREV_RELATIVE);
      tvTimestamp.setText(time);

      if (entity.unreadCount > 0) {
        tvUnread.setVisibility(View.VISIBLE);
        tvUnread.setText(String.format(Locale.getDefault(), "%d", entity.unreadCount));
      } else {
        tvUnread.setVisibility(View.GONE);
      }

      itemView.setBackgroundResource(selected ? R.drawable.bg_chat_thread_selected : R.drawable.bg_chat_thread_default);
    }
  }
}
