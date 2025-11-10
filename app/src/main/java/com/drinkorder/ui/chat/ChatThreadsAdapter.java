// Khai báo package com.drinkorder.ui.chat cho toàn bộ lớp.
package com.drinkorder.ui.chat;

// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.text.format.DateUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.format.DateUtils;
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
// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;

// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp ChatThreadsAdapter kế thừa ListAdapter<ChatThreadEntity,.
public class ChatThreadsAdapter extends ListAdapter<ChatThreadEntity, ChatThreadsAdapter.ThreadViewHolder> {

  // Định nghĩa interface Listener.
  public interface Listener {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onThreadSelected(@NonNull ChatThreadEntity thread);.
    void onThreadSelected(@NonNull ChatThreadEntity thread);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final Listener listener.
  private final Listener listener;
  // Khai báo thuộc tính với phạm vi truy cập: private String selectedThreadId.
  private String selectedThreadId;
  // Khai báo thuộc tính với phạm vi truy cập: private String localRole.
  private String localRole;

  // Định nghĩa phương thức ChatThreadsAdapter với phạm vi truy cập tương ứng.
  public ChatThreadsAdapter(@NonNull Listener listener) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(DIFF_CALLBACK);.
    super(DIFF_CALLBACK);
    // Gán giá trị cho biến hoặc thuộc tính: this.listener = listener.
    this.listener = listener;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Thực thi câu lệnh: private static final DiffUtil.ItemCallback<ChatThreadEntity> DIFF_CALLBACK =.
  private static final DiffUtil.ItemCallback<ChatThreadEntity> DIFF_CALLBACK =
      // Khởi tạo đối tượng mới với biểu thức new DiffUtil.ItemCallback<>() {.
      new DiffUtil.ItemCallback<>() {
        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areItemsTheSame với phạm vi truy cập tương ứng.
        public boolean areItemsTheSame(@NonNull ChatThreadEntity oldItem, @NonNull ChatThreadEntity newItem) {
          // Trả về kết quả TextUtils.equals(oldItem.threadId, newItem.threadId);.
          return TextUtils.equals(oldItem.threadId, newItem.threadId);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }

        // Áp dụng annotation @Override cho phần tử bên dưới.
        @Override
        // Định nghĩa phương thức areContentsTheSame với phạm vi truy cập tương ứng.
        public boolean areContentsTheSame(@NonNull ChatThreadEntity oldItem, @NonNull ChatThreadEntity newItem) {
          // Trả về kết quả TextUtils.equals(oldItem.title, newItem.title).
          return TextUtils.equals(oldItem.title, newItem.title)
              // Thực hiện lời gọi phương thức hoặc khởi tạo: && TextUtils.equals(oldItem.lastMessage, newItem.lastMessage).
              && TextUtils.equals(oldItem.lastMessage, newItem.lastMessage)
              // Thực hiện lời gọi phương thức hoặc khởi tạo: && TextUtils.equals(oldItem.lastSenderRole, newItem.lastSenderRole).
              && TextUtils.equals(oldItem.lastSenderRole, newItem.lastSenderRole)
              // Thực thi câu lệnh: && oldItem.lastTimestamp == newItem.lastTimestamp.
              && oldItem.lastTimestamp == newItem.lastTimestamp
              // Gán giá trị cho biến hoặc thuộc tính: && oldItem.unreadCount == newItem.unreadCount.
              && oldItem.unreadCount == newItem.unreadCount;
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực thi câu lệnh: };.
      };

  // Định nghĩa phương thức setSelectedThreadId với phạm vi truy cập tương ứng.
  public void setSelectedThreadId(String threadId) {
    // Gán giá trị cho biến hoặc thuộc tính: selectedThreadId = threadId.
    selectedThreadId = threadId;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
    notifyDataSetChanged();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setLocalRole với phạm vi truy cập tương ứng.
  public void setLocalRole(String role) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!TextUtils.equals(localRole, role)) {
      // Gán giá trị cho biến hoặc thuộc tính: localRole = role.
      localRole = role;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: notifyDataSetChanged();.
      notifyDataSetChanged();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @NonNull cho phần tử bên dưới.
  @NonNull
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateViewHolder với phạm vi truy cập tương ứng.
  public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_thread, parent, false);.
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_thread, parent, false);
    // Trả về kết quả new ThreadViewHolder(view);.
    return new ThreadViewHolder(view);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onBindViewHolder với phạm vi truy cập tương ứng.
  public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity item = getItem(position);.
    ChatThreadEntity item = getItem(position);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (item == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: holder.bind(item, TextUtils.equals(item.threadId, selectedThreadId), localRole);.
    holder.bind(item, TextUtils.equals(item.threadId, selectedThreadId), localRole);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    holder.itemView.setOnClickListener(v -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: int adapterPosition = holder.getBindingAdapterPosition();.
      int adapterPosition = holder.getBindingAdapterPosition();
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (adapterPosition == RecyclerView.NO_POSITION) { return; }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: ChatThreadEntity clicked = getItem(adapterPosition);.
      ChatThreadEntity clicked = getItem(adapterPosition);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (clicked != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: listener.onThreadSelected(clicked);.
        listener.onThreadSelected(clicked);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa lớp ThreadViewHolder kế thừa RecyclerView.ViewHolder.
  static class ThreadViewHolder extends RecyclerView.ViewHolder {
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvTitle.
    private final TextView tvTitle;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvPreview.
    private final TextView tvPreview;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvTimestamp.
    private final TextView tvTimestamp;
    // Khai báo thuộc tính với phạm vi truy cập: private final TextView tvUnread.
    private final TextView tvUnread;

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    ThreadViewHolder(@NonNull View itemView) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: super(itemView);.
      super(itemView);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTitle = itemView.findViewById(R.id.tvThreadTitle);.
      tvTitle = itemView.findViewById(R.id.tvThreadTitle);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPreview = itemView.findViewById(R.id.tvThreadPreview);.
      tvPreview = itemView.findViewById(R.id.tvThreadPreview);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTimestamp = itemView.findViewById(R.id.tvThreadTimestamp);.
      tvTimestamp = itemView.findViewById(R.id.tvThreadTimestamp);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUnread = itemView.findViewById(R.id.tvUnreadBadge);.
      tvUnread = itemView.findViewById(R.id.tvUnreadBadge);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    void bind(@NonNull ChatThreadEntity entity, boolean selected, @Nullable String localRole) {
      // Gán giá trị cho biến hoặc thuộc tính: String title = entity.title.
      String title = entity.title;
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (TextUtils.isEmpty(title)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: title = itemView.getContext().getString(R.string.chat_default_title);.
        title = itemView.getContext().getString(R.string.chat_default_title);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTitle.setText(title);.
      tvTitle.setText(title);

      // Gán giá trị cho biến hoặc thuộc tính: String preview = entity.lastMessage.
      String preview = entity.lastMessage;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String senderLabel = buildSenderLabel(localRole, entity);.
      String senderLabel = buildSenderLabel(localRole, entity);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (!TextUtils.isEmpty(preview) && !TextUtils.isEmpty(senderLabel)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: preview = itemView.getContext().getString(R.string.chat_thread_preview_sender, senderLabel, preview);.
        preview = itemView.getContext().getString(R.string.chat_thread_preview_sender, senderLabel, preview);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else if (TextUtils.isEmpty(preview)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: preview = itemView.getContext().getString(R.string.chat_default_subtitle);.
        preview = itemView.getContext().getString(R.string.chat_default_subtitle);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvPreview.setText(preview);.
      tvPreview.setText(preview);

      // Thực thi câu lệnh: CharSequence time = DateUtils.getRelativeTimeSpanString(.
      CharSequence time = DateUtils.getRelativeTimeSpanString(
          // Thực thi câu lệnh: entity.lastTimestamp,.
          entity.lastTimestamp,
          // Thực thi câu lệnh: System.currentTimeMillis(),.
          System.currentTimeMillis(),
          // Thực thi câu lệnh: DateUtils.MINUTE_IN_MILLIS,.
          DateUtils.MINUTE_IN_MILLIS,
          // Thực hiện lời gọi phương thức hoặc khởi tạo: DateUtils.FORMAT_ABBREV_RELATIVE);.
          DateUtils.FORMAT_ABBREV_RELATIVE);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvTimestamp.setText(time);.
      tvTimestamp.setText(time);

      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (entity.unreadCount > 0) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUnread.setVisibility(View.VISIBLE);.
        tvUnread.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUnread.setText(String.format(Locale.getDefault(), "%d", entity.unreadCount));.
        tvUnread.setText(String.format(Locale.getDefault(), "%d", entity.unreadCount));
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvUnread.setVisibility(View.GONE);.
        tvUnread.setVisibility(View.GONE);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Thực hiện lời gọi phương thức hoặc khởi tạo: itemView.setBackgroundResource(selected ? R.drawable.bg_chat_thread_selected : R.drawable.bg_chat_thread_default);.
      itemView.setBackgroundResource(selected ? R.drawable.bg_chat_thread_selected : R.drawable.bg_chat_thread_default);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Định nghĩa phương thức buildSenderLabel với phạm vi truy cập tương ứng.
    private String buildSenderLabel(@Nullable String localRole, @NonNull ChatThreadEntity entity) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (TextUtils.isEmpty(entity.lastSenderRole)) {
        // Trả về kết quả null;.
        return null;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (!TextUtils.isEmpty(localRole) && entity.lastSenderRole.equalsIgnoreCase(localRole)) {
        // Trả về kết quả itemView.getContext().getString(R.string.chat_role_you);.
        return itemView.getContext().getString(R.string.chat_role_you);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if ("support".equalsIgnoreCase(entity.lastSenderRole) || "admin".equalsIgnoreCase(entity.lastSenderRole)) {
        // Trả về kết quả itemView.getContext().getString(R.string.chat_role_support);.
        return itemView.getContext().getString(R.string.chat_role_support);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Trả về kết quả itemView.getContext().getString(R.string.chat_role_customer);.
      return itemView.getContext().getString(R.string.chat_role_customer);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
