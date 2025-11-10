package com.drinkorder.ui.chat.customer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.drinkorder.databinding.ItemCustomerChatMessageIncomingBinding;
import com.drinkorder.databinding.ItemCustomerChatMessageOutgoingBinding;

public class CustomerChatMessageAdapter
    extends ListAdapter<CustomerChatMessageItem, RecyclerView.ViewHolder> {

  private static final int TYPE_OUTGOING = 1;
  private static final int TYPE_INCOMING = 2;

  public CustomerChatMessageAdapter() {
    super(DIFF_CALLBACK);
  }

  private static final DiffUtil.ItemCallback<CustomerChatMessageItem> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<CustomerChatMessageItem>() {
        @Override
        public boolean areItemsTheSame(
            @NonNull CustomerChatMessageItem oldItem, @NonNull CustomerChatMessageItem newItem) {
          return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(
            @NonNull CustomerChatMessageItem oldItem, @NonNull CustomerChatMessageItem newItem) {
          return oldItem.isMine() == newItem.isMine()
              && equals(oldItem.getMessage(), newItem.getMessage())
              && equals(oldItem.getTimestamp(), newItem.getTimestamp());
        }

        private boolean equals(String a, String b) {
          if (a == null) return b == null;
          return a.equals(b);
        }
      };

  @Override
  public int getItemViewType(int position) {
    CustomerChatMessageItem item = getItem(position);
    return item != null && item.isMine() ? TYPE_OUTGOING : TYPE_INCOMING;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == TYPE_OUTGOING) {
      ItemCustomerChatMessageOutgoingBinding binding =
          ItemCustomerChatMessageOutgoingBinding.inflate(inflater, parent, false);
      return new OutgoingHolder(binding);
    }
    ItemCustomerChatMessageIncomingBinding binding =
        ItemCustomerChatMessageIncomingBinding.inflate(inflater, parent, false);
    return new IncomingHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    CustomerChatMessageItem item = getItem(position);
    if (item == null) return;
    if (holder instanceof OutgoingHolder outgoingHolder) {
      outgoingHolder.bind(item);
    } else if (holder instanceof IncomingHolder incomingHolder) {
      incomingHolder.bind(item);
    }
  }

  private static class OutgoingHolder extends RecyclerView.ViewHolder {
    private final ItemCustomerChatMessageOutgoingBinding binding;

    OutgoingHolder(ItemCustomerChatMessageOutgoingBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(CustomerChatMessageItem item) {
      binding.tvMessage.setText(item.getMessage());
      binding.tvTimestamp.setText(item.getTimestamp());
    }
  }

  private static class IncomingHolder extends RecyclerView.ViewHolder {
    private final ItemCustomerChatMessageIncomingBinding binding;

    IncomingHolder(ItemCustomerChatMessageIncomingBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(CustomerChatMessageItem item) {
      binding.tvMessage.setText(item.getMessage());
      binding.tvTimestamp.setText(item.getTimestamp());
    }
  }
}
