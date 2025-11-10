package com.drinkorder.ui.chat.admin;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.R;
import com.drinkorder.data.model.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminChatMessageAdapter extends RecyclerView.Adapter<AdminChatMessageAdapter.MessageHolder> {

  private final List<ChatMessage> items = new ArrayList<>();

  public void submit(List<ChatMessage> list) {
    items.clear();
    if (list != null) items.addAll(list);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_admin_chat_message, parent, false);
    return new MessageHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
    holder.bind(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class MessageHolder extends RecyclerView.ViewHolder {
    private final LinearLayout messageContainer;
    private final TextView tvMessage;
    private final TextView tvTime;

    MessageHolder(@NonNull View itemView) {
      super(itemView);
      messageContainer = itemView.findViewById(R.id.messageContainer);
      tvMessage = itemView.findViewById(R.id.tvMessageContent);
      tvTime = itemView.findViewById(R.id.tvMessageTime);
    }

    void bind(ChatMessage message) {
      boolean fromAdmin = message.getSenderRole() == ChatMessage.SenderRole.ADMIN;
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) messageContainer.getLayoutParams();
      int margin = (int) (itemView.getResources().getDisplayMetrics().density * 64);
      params.gravity = fromAdmin ? android.view.Gravity.END : android.view.Gravity.START;
      params.setMargins(fromAdmin ? margin : 0, params.topMargin, fromAdmin ? 0 : margin, params.bottomMargin);
      messageContainer.setLayoutParams(params);

      tvMessage.setBackgroundResource(fromAdmin ? R.drawable.bg_chat_bubble_admin : R.drawable.bg_chat_bubble_customer);
      int textColor = ContextCompat.getColor(itemView.getContext(),
          fromAdmin ? R.color.brand_on_primary : R.color.brand_on_surface);
      tvMessage.setTextColor(textColor);
      tvMessage.setText(message.getContent());
      tvTime.setText(DateFormat.format("HH:mm", new Date(message.getTimestamp())));
      tvTime.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.brand_muted_text));
    }
  }
}
