// Đặt package để fragment thuộc nhóm chức năng trò chuyện.
package com.drinkorder.ui.chat;

// Import Bundle cho vòng đời fragment.
import android.os.Bundle;
// Import Editable để đọc nội dung text từ TextWatcher.
import android.text.Editable;
// Import TextUtils để xử lý chuỗi tiện lợi.
import android.text.TextUtils;
// Import TextWatcher để theo dõi ô nhập tin nhắn.
import android.text.TextWatcher;
// Import LayoutInflater để tạo view từ XML.
import android.view.LayoutInflater;
// Import View để thao tác với các phần tử giao diện.
import android.view.View;
// Import ViewGroup làm container cho fragment.
import android.view.ViewGroup;
// Import ImageButton cho nút gửi tin nhắn.
import android.widget.ImageButton;
// Import TextView để hiển thị trạng thái kết nối và thông tin hội thoại.
import android.widget.TextView;

// Import NonNull chú thích tham số bắt buộc có giá trị.
import androidx.annotation.NonNull;
// Import Nullable chú thích tham số có thể rỗng.
import androidx.annotation.Nullable;
// Import Fragment làm lớp cơ sở cho màn trò chuyện.
import androidx.fragment.app.Fragment;
// Import ViewModelProvider để lấy ChatViewModel dùng chung.
import androidx.lifecycle.ViewModelProvider;
// Import LinearLayoutManager để hiển thị danh sách tin nhắn từ dưới lên.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import RecyclerView để hiển thị luồng tin nhắn.
import androidx.recyclerview.widget.RecyclerView;

// Import R để truy cập layout và string resource của module chat.
import com.drinkorder.R;
// Import ChatMessageEntity để hiển thị nội dung tin nhắn.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
// Import ChatThreadEntity để hiển thị thông tin cuộc trò chuyện.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
// Import ChatSocketClient để nhận trạng thái kết nối realtime.
import com.drinkorder.data.remote.ChatSocketClient;
// Import TextInputEditText làm ô nhập tin nhắn.
import com.google.android.material.textfield.TextInputEditText;

// Import List để làm việc với danh sách tin nhắn.
import java.util.List;

// Fragment chịu trách nhiệm hiển thị luồng chat và xử lý gửi tin nhắn.
public class ChatFragment extends Fragment {

  // ViewModel quản lý dữ liệu hội thoại và trạng thái socket.
  private ChatViewModel viewModel;
  // Adapter hiển thị danh sách tin nhắn.
  private ChatMessagesAdapter adapter;
  // RecyclerView hiển thị các tin nhắn.
  private RecyclerView rvMessages;
  // TextView thông báo trạng thái kết nối socket.
  private TextView tvConnectionStatus;
  // TextView hiển thị tiêu đề cuộc trò chuyện.
  private TextView tvThreadTitle;
  // TextView hiển thị mô tả phụ của cuộc trò chuyện.
  private TextView tvThreadSubtitle;
  // TextView hiển thị empty state khi không có tin nhắn hoặc cuộc trò chuyện.
  private TextView tvEmptyState;
  // Ô nhập tin nhắn mới.
  private TextInputEditText edtMessage;
  // Nút gửi tin nhắn.
  private ImageButton btnSend;
  // Cờ đánh dấu đã có cuộc trò chuyện đang hoạt động hay chưa.
  private boolean hasActiveThread = false;

  // TextWatcher để kích hoạt nút gửi khi người dùng nhập nội dung.
  private final TextWatcher textWatcher = new TextWatcher() {
    // Không cần xử lý trước khi nội dung thay đổi.
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    // Khi nội dung thay đổi cần cập nhật trạng thái nút gửi.
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      updateSendButtonState();
    }

    // Không cần xử lý sau khi nội dung thay đổi.
    @Override
    public void afterTextChanged(Editable s) { }
  };

  // Inflate layout chat và ánh xạ view.
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Tạo view từ layout fragment_chat.
    View view = inflater.inflate(R.layout.fragment_chat, container, false);
    // Ánh xạ RecyclerView hiển thị tin nhắn.
    rvMessages = view.findViewById(R.id.rvMessages);
    // Ánh xạ TextView trạng thái kết nối socket.
    tvConnectionStatus = view.findViewById(R.id.tvConnectionStatus);
    // Ánh xạ TextView tiêu đề cuộc trò chuyện.
    tvThreadTitle = view.findViewById(R.id.tvThreadTitle);
    // Ánh xạ TextView phụ mô tả cuộc trò chuyện.
    tvThreadSubtitle = view.findViewById(R.id.tvThreadSubtitle);
    // Ánh xạ TextView hiển thị empty state.
    tvEmptyState = view.findViewById(R.id.tvEmptyState);
    // Ánh xạ ô nhập tin nhắn.
    edtMessage = view.findViewById(R.id.edtMessage);
    // Ánh xạ nút gửi tin nhắn.
    btnSend = view.findViewById(R.id.btnSend);

    // Dùng LinearLayoutManager và đảo chiều để tin nhắn mới ở cuối danh sách.
    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    layoutManager.setStackFromEnd(true);
    rvMessages.setLayoutManager(layoutManager);
    // Khởi tạo adapter và gắn vào RecyclerView.
    adapter = new ChatMessagesAdapter();
    rvMessages.setAdapter(adapter);

    // Gửi tin nhắn khi người dùng bấm nút gửi.
    btnSend.setOnClickListener(v -> {
      if (viewModel != null) {
        // Lấy nội dung hiện tại trong ô nhập.
        CharSequence text = edtMessage.getText();
        // Gửi tin nhắn qua ViewModel.
        viewModel.sendMessage(text != null ? text.toString() : null);
        // Xóa nội dung ô nhập sau khi gửi.
        edtMessage.setText("");
      }
    });

    // Lắng nghe thay đổi nội dung ô nhập để điều chỉnh nút gửi.
    edtMessage.addTextChangedListener(textWatcher);

    // Trả về view đã cấu hình.
    return view;
  }

  // Thiết lập ViewModel và đăng ký observer sau khi view được tạo.
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Gọi super để hoàn tất vòng đời chuẩn.
    super.onViewCreated(view, savedInstanceState);
    // Lấy ChatViewModel chia sẻ với Activity để đồng bộ dữ liệu chat.
    viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
    // Cập nhật tên hiển thị của người gửi cục bộ trong adapter.
    if (adapter != null) {
      adapter.setLocalDisplayName(viewModel.getLocalDisplayName());
    }

    // Quan sát trạng thái kết nối socket để cập nhật thông báo.
    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);
    // Quan sát thread đang hoạt động để cập nhật tiêu đề và trạng thái gửi.
    viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);
    // Quan sát danh sách tin nhắn và đổ vào RecyclerView.
    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
    // Quan sát danh sách cuộc trò chuyện để hiển thị empty state phù hợp.
    viewModel.getThreads().observe(getViewLifecycleOwner(), threads -> {
      if (threads == null || threads.isEmpty()) {
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText(R.string.chat_empty_state);
      }
    });
  }

  // Kết nối socket khi fragment hiển thị.
  @Override
  public void onStart() {
    super.onStart();
    if (viewModel != null) {
      viewModel.connect();
    }
  }

  // Ngắt kết nối khi fragment dừng để tiết kiệm tài nguyên.
  @Override
  public void onStop() {
    super.onStop();
    if (viewModel != null) {
      viewModel.disconnect();
    }
  }

  // Dọn dẹp listener để tránh rò rỉ bộ nhớ khi view bị hủy.
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    edtMessage.removeTextChangedListener(textWatcher);
    rvMessages.setAdapter(null);
    adapter = null;
  }

  // Cập nhật thông báo trạng thái kết nối theo phản hồi từ socket.
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

  // Cập nhật thông tin tiêu đề khi người dùng chọn thread khác nhau.
  private void renderActiveThread(@Nullable ChatThreadEntity thread) {
    hasActiveThread = thread != null;
    if (thread == null) {
      tvThreadTitle.setText(R.string.chat_default_title);
      tvThreadSubtitle.setText(R.string.chat_default_subtitle);
      if (adapter != null) {
        adapter.setRemoteDisplayName(getString(R.string.chat_default_title));
      }
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
      if (adapter != null) {
        adapter.setRemoteDisplayName(partnerName);
      }
    }
    updateSendButtonState();
  }

  // Hiển thị danh sách tin nhắn và cuộn tới tin mới nhất.
  private void renderMessages(@Nullable List<ChatMessageEntity> messages) {
    if (adapter == null) return;
    adapter.submitSafeList(messages);
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

  // Bật tắt nút gửi dựa trên trạng thái thread và nội dung nhập liệu.
  private void updateSendButtonState() {
    if (btnSend == null) return;
    CharSequence text = edtMessage != null ? edtMessage.getText() : null;
    boolean hasContent = text != null && text.length() > 0;
    btnSend.setEnabled(hasActiveThread && hasContent);
  }
}
