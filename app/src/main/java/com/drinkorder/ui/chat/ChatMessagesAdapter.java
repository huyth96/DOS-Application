// Khai báo package com.drinkorder.ui.chat cho toàn bộ lớp.
package com.drinkorder.ui.chat;

// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.recyclerview.widget.DiffUtil để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.DiffUtil;
// Import androidx.recyclerview.widget.ListAdapter để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.ListAdapter;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;

// Import java.text.DateFormat để sử dụng các lớp hoặc hàm tương ứng.
import java.text.DateFormat;
// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.Date để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Date;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp ChatMessagesAdapter kế thừa ListAdapter<ChatMessageEntity,.
public class ChatMessagesAdapter extends ListAdapter<ChatMessageEntity, RecyclerView.ViewHolder> {

  // Khai báo thuộc tính với phạm vi truy cập: private static final int TYPE_INCOMING = 0.
  private static final int TYPE_INCOMING = 0;
  // Khai báo thuộc tính với phạm vi truy cập: private static final int TYPE_OUTGOING = 1.
  private static final int TYPE_OUTGOING = 1;

  // Khai báo thuộc tính với phạm vi truy cập: private String localDisplayName.
  private String localDisplayName;
  // Khai báo thuộc tính với phạm vi truy cập: private String remoteDisplayName.
  private String remoteDisplayName;

  // Định nghĩa phương thức ChatMessagesAdapter với phạm vi truy cập tương ứng.
  public ChatMessagesAdapter() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(DIFF_CALLBACK);.
    super(DIFF_CALLBACK);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setLocalDisplayName với phạm vi truy cập tương ứng.
  public void setLocalDisplayName(@Nullable String displayName) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.equals(localDisplayName, displayName)) {
      // Gán giá trị cho biến hoặc thuộc tính: localDisplayName = displayName.
      localDisplayName = displayName;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
      notifyDataSetChanged();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setRemoteDisplayName với phạm vi truy cập tương ứng.
  public void setRemoteDisplayName(@Nullable String displayName) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.equals(remoteDisplayName, displayName)) {
      // Gán giá trị cho biến hoặc thuộc tính: remoteDisplayName = displayName.
      remoteDisplayName = displayName;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
      notifyDataSetChanged();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức submitSafeList với phạm vi truy cập tương ứng.
  public void submitSafeList(@Nullable java.util.List<ChatMessageEntity> entries) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (entries == null || entries.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: submitList(java.util.Collections.emptyList());.
      submitList(java.util.Collections.emptyList());
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: submitList(new ArrayList<>(entries));.
      submitList(new ArrayList<>(entries));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Thực thi câu lệnh: private static final DiffUtil.ItemCallback<ChatMessageEntity> DIFF_CALLBACK =.
  private static final DiffUtil.ItemCallback<ChatMessageEntity> DIFF_CALLBACK =
      // Khởi tạo đối tượng mới với biểu thức new DiffUtil.ItemCallback<>() {.
      new DiffUtil.ItemCallback<>() {
        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areItemsTheSame với phạm vi truy cập tương ứng.
        public boolean areItemsTheSame(@NonNull ChatMessageEntity oldItem, @NonNull ChatMessageEntity newItem) {
          // Trả về kết quả TextUtils.equals(oldItem.messageId, newItem.messageId);.
          return TextUtils.equals(oldItem.messageId, newItem.messageId);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areContentsTheSame với phạm vi truy cập tương ứng.
        public boolean areContentsTheSame(@NonNull ChatMessageEntity oldItem, @NonNull ChatMessageEntity newItem) {
          // Trả về kết quả TextUtils.equals(oldItem.body, newItem.body).
          return TextUtils.equals(oldItem.body, newItem.body)
              // Thực thi câu lệnh: && oldItem.sentAt == newItem.sentAt.
              && oldItem.sentAt == newItem.sentAt
              // Thực thi câu lệnh: && oldItem.isOutgoing == newItem.isOutgoing.
              && oldItem.isOutgoing == newItem.isOutgoing
              // Thực thi câu lệnh: && oldItem.isPending == newItem.isPending.
              && oldItem.isPending == newItem.isPending
              // Thực hiện lời gọi phương thức hoặc khởi tạo: && equalsLong(oldItem.deliveredAt, newItem.deliveredAt);.
              && equalsLong(oldItem.deliveredAt, newItem.deliveredAt);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực thi câu lệnh: };.
      };

  // Định nghĩa phương thức equalsLong với phạm vi truy cập tương ứng.
  private static boolean equalsLong(Long a, Long b) {
    // Trả về kết quả a == b || (a != null && a.equals(b));.
    return a == b || (a != null && a.equals(b));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức getItemViewType với phạm vi truy cập tương ứng.
  public int getItemViewType(int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatMessageEntity item = getItem(position);.
    ChatMessageEntity item = getItem(position);
    // Trả về kết quả item != null && item.isOutgoing ? TYPE_OUTGOING : TYPE_INCOMING;.
    return item != null && item.isOutgoing ? TYPE_OUTGOING : TYPE_INCOMING;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: LayoutInflater inflater = LayoutInflater.from(parent.getContext());.
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (viewType == TYPE_OUTGOING) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = inflater.inflate(R.layout.item_chat_message_outgoing, parent, false);.
      View view = inflater.inflate(R.layout.item_chat_message_outgoing, parent, false);
      // Trả về kết quả new OutgoingMessageViewHolder(view);.
      return new OutgoingMessageViewHolder(view);
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = inflater.inflate(R.layout.item_chat_message_incoming, parent, false);.
      View view = inflater.inflate(R.layout.item_chat_message_incoming, parent, false);
      // Trả về kết quả new IncomingMessageViewHolder(view);.
      return new IncomingMessageViewHolder(view);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatMessageEntity item = getItem(position);.
    ChatMessageEntity item = getItem(position);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (item == null) { return; }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String senderLabel = resolveSenderLabel(holder.itemView, item);.
    String senderLabel = resolveSenderLabel(holder.itemView, item);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String metaLabel = buildMeta(holder.itemView, item);.
    String metaLabel = buildMeta(holder.itemView, item);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (holder instanceof OutgoingMessageViewHolder outgoing) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: outgoing.bind(item, senderLabel, metaLabel);.
      outgoing.bind(item, senderLabel, metaLabel);
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (holder instanceof IncomingMessageViewHolder incoming) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: incoming.bind(item, senderLabel, metaLabel);.
      incoming.bind(item, senderLabel, metaLabel);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức resolveSenderLabel với phạm vi truy cập tương ứng.
  private String resolveSenderLabel(@NonNull View itemView, @NonNull ChatMessageEntity entity) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (entity.isOutgoing) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (!TextUtils.isEmpty(localDisplayName)) {
        // Trả về kết quả itemView.getContext().getString(R.string.chat_sender_you_with_name, localDisplayName);.
        return itemView.getContext().getString(R.string.chat_sender_you_with_name, localDisplayName);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Trả về kết quả itemView.getContext().getString(R.string.chat_role_you);.
      return itemView.getContext().getString(R.string.chat_role_you);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.isEmpty(remoteDisplayName)) {
      // Trả về kết quả remoteDisplayName;.
      return remoteDisplayName;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if ("support".equalsIgnoreCase(entity.senderRole) || "admin".equalsIgnoreCase(entity.senderRole)) {
      // Trả về kết quả itemView.getContext().getString(R.string.chat_role_support);.
      return itemView.getContext().getString(R.string.chat_role_support);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả itemView.getContext().getString(R.string.chat_role_customer);.
    return itemView.getContext().getString(R.string.chat_role_customer);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức formatTime với phạm vi truy cập tương ứng.
  private static String formatTime(long timestamp) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());.
    DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
    // Trả về kết quả format.format(new Date(timestamp));.
    return format.format(new Date(timestamp));
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức buildMeta với phạm vi truy cập tương ứng.
  private static String buildMeta(@NonNull View itemView, @NonNull ChatMessageEntity entity) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: StringBuilder meta = new StringBuilder(formatTime(entity.sentAt));.
    StringBuilder meta = new StringBuilder(formatTime(entity.sentAt));
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (entity.isPending) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: meta.append(" ").
      meta.append(" ")
          // Thực hiện lời gọi phương thức hoặc khởi tạo: .append(itemView.getContext().getString(R.string.chat_meta_separator)).
          .append(itemView.getContext().getString(R.string.chat_meta_separator))
          // Thực hiện lời gọi phương thức hoặc khởi tạo: .append(" ").
          .append(" ")
          // Thực hiện lời gọi phương thức hoặc khởi tạo: .append(itemView.getContext().getString(R.string.chat_message_pending));.
          .append(itemView.getContext().getString(R.string.chat_message_pending));
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả meta.toString();.
    return meta.toString();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp IncomingMessageViewHolder kế thừa RecyclerView.ViewHolder.
  private static class IncomingMessageViewHolder extends RecyclerView.ViewHolder {
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvSender.
    private final TextView tvSender;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvBody.
    private final TextView tvBody;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvMeta.
    private final TextView tvMeta;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    IncomingMessageViewHolder(@NonNull View itemView) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(itemView);.
      super(itemView);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSender = itemView.findViewById(R.id.tvSender);.
      tvSender = itemView.findViewById(R.id.tvSender);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBody = itemView.findViewById(R.id.tvBody);.
      tvBody = itemView.findViewById(R.id.tvBody);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta = itemView.findViewById(R.id.tvMeta);.
      tvMeta = itemView.findViewById(R.id.tvMeta);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    void bind(@NonNull ChatMessageEntity entity, @NonNull String senderLabel, @NonNull String metaLabel) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSender.setText(senderLabel);.
      tvSender.setText(senderLabel);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBody.setText(entity.body == null ? "" : entity.body);.
      tvBody.setText(entity.body == null ? "" : entity.body);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta.setText(metaLabel);.
      tvMeta.setText(metaLabel);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp OutgoingMessageViewHolder kế thừa RecyclerView.ViewHolder.
  private static class OutgoingMessageViewHolder extends RecyclerView.ViewHolder {
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvSender.
    private final TextView tvSender;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvBody.
    private final TextView tvBody;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvMeta.
    private final TextView tvMeta;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    OutgoingMessageViewHolder(@NonNull View itemView) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(itemView);.
      super(itemView);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSender = itemView.findViewById(R.id.tvSender);.
      tvSender = itemView.findViewById(R.id.tvSender);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBody = itemView.findViewById(R.id.tvBody);.
      tvBody = itemView.findViewById(R.id.tvBody);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta = itemView.findViewById(R.id.tvMeta);.
      tvMeta = itemView.findViewById(R.id.tvMeta);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    void bind(@NonNull ChatMessageEntity entity, @NonNull String senderLabel, @NonNull String metaLabel) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSender.setText(senderLabel);.
      tvSender.setText(senderLabel);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvBody.setText(entity.body == null ? "" : entity.body);.
      tvBody.setText(entity.body == null ? "" : entity.body);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvMeta.setText(metaLabel);.
      tvMeta.setText(metaLabel);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
