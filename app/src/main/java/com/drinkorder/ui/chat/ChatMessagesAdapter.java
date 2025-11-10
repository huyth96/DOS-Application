package com.drinkorder.ui.chat;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessagesAdapter extends ListAdapter<ChatMessageEntity, RecyclerView.ViewHolder> {

  private static final int TYPE_INCOMING = 0;
  private static final int TYPE_OUTGOING = 1;

  public ChatMessagesAdapter() {
    super(DIFF_CALLBACK);
  }

  private static final DiffUtil.ItemCallback<ChatMessageEntity> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatMessageEntity oldItem, @NonNull ChatMessageEntity newItem) {
          return TextUtils.equals(oldItem.messageId, newItem.messageId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessageEntity oldItem, @NonNull ChatMessageEntity newItem) {
          return TextUtils.equals(oldItem.body, newItem.body)
              && oldItem.sentAt == newItem.sentAt
              && oldItem.isOutgoing == newItem.isOutgoing
              && oldItem.isPending == newItem.isPending
              && equalsLong(oldItem.deliveredAt, newItem.deliveredAt);
        }
      };

  private static boolean equalsLong(Long a, Long b) {
    return a == b || (a != null && a.equals(b));
  }

  @Override
  public int getItemViewType(int position) {
    ChatMessageEntity item = getItem(position);
    return item != null && item.isOutgoing ? TYPE_OUTGOING : TYPE_INCOMING;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == TYPE_OUTGOING) {
      View view = inflater.inflate(R.layout.item_chat_message_outgoing, parent, false);
      return new OutgoingMessageViewHolder(view);
    } else {
      View view = inflater.inflate(R.layout.item_chat_message_incoming, parent, false);
      return new IncomingMessageViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ChatMessageEntity item = getItem(position);
    if (item == null) return;
    if (holder instanceof OutgoingMessageViewHolder outgoing) {
      outgoing.bind(item);
    } else if (holder instanceof IncomingMessageViewHolder incoming) {
      incoming.bind(item);
    }
  }

  private static String formatTime(long timestamp) {
    DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
    return format.format(new Date(timestamp));
  }

  private static class IncomingMessageViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvBody;
    private final TextView tvMeta;

    IncomingMessageViewHolder(@NonNull View itemView) {
      super(itemView);
      tvBody = itemView.findViewById(R.id.tvBody);
      tvMeta = itemView.findViewById(R.id.tvMeta);
    }

    void bind(@NonNull ChatMessageEntity entity) {
      tvBody.setText(entity.body == null ? "" : entity.body);
      tvMeta.setText(formatTime(entity.sentAt));
    }
  }

  private static class OutgoingMessageViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvBody;
    private final TextView tvMeta;

    OutgoingMessageViewHolder(@NonNull View itemView) {
      super(itemView);
      tvBody = itemView.findViewById(R.id.tvBody);
      tvMeta = itemView.findViewById(R.id.tvMeta);
    }

    void bind(@NonNull ChatMessageEntity entity) {
      tvBody.setText(entity.body == null ? "" : entity.body);
      StringBuilder meta = new StringBuilder(formatTime(entity.sentAt));
      if (entity.isPending) {
        meta.append(" • ").append(itemView.getContext().getString(R.string.chat_message_pending));
      }
      tvMeta.setText(meta.toString());
    }
  }
}
