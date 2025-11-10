// Khai báo package com.drinkorder.ui.chat cho toàn bộ lớp.
package com.drinkorder.ui.chat;

// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.text.Editable để sử dụng các lớp hoặc hàm tương ứng.
import android.text.Editable;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.text.TextWatcher để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextWatcher;
// Import android.view.LayoutInflater để sử dụng các lớp hoặc hàm tương ứng.
import android.view.LayoutInflater;
// Import android.view.View để sử dụng các lớp hoặc hàm tương ứng.
import android.view.View;
// Import android.view.ViewGroup để sử dụng các lớp hoặc hàm tương ứng.
import android.view.ViewGroup;
// Import android.widget.ImageButton để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.ImageButton;
// Import android.widget.TextView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.TextView;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.fragment.app.Fragment để sử dụng các lớp hoặc hàm tương ứng.
import androidx.fragment.app.Fragment;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;
// Import androidx.recyclerview.widget.LinearLayoutManager để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.LinearLayoutManager;
// Import androidx.recyclerview.widget.RecyclerView để sử dụng các lớp hoặc hàm tương ứng.
import androidx.recyclerview.widget.RecyclerView;

// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.chat.ChatMessageEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatMessageEntity;
// Import com.drinkorder.data.db.entity.chat.ChatThreadEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.chat.ChatThreadEntity;
// Import com.drinkorder.data.remote.ChatSocketClient để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.remote.ChatSocketClient;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Định nghĩa lớp ChatFragment kế thừa Fragment.
public class ChatFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private ChatViewModel viewModel.
  private ChatViewModel viewModel;
  // Khai báo thuộc tính với phạm vi truy cập: private ChatMessagesAdapter adapter.
  private ChatMessagesAdapter adapter;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView rvMessages.
  private RecyclerView rvMessages;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvConnectionStatus.
  private TextView tvConnectionStatus;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvThreadTitle.
  private TextView tvThreadTitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvThreadSubtitle.
  private TextView tvThreadSubtitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptyState.
  private TextView tvEmptyState;
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtMessage.
  private TextInputEditText edtMessage;
  // Khai báo thuộc tính với phạm vi truy cập: private ImageButton btnSend.
  private ImageButton btnSend;
  // Khai báo thuộc tính với phạm vi truy cập: private boolean hasActiveThread = false.
  private boolean hasActiveThread = false;

  // Định nghĩa phương thức TextWatcher với phạm vi truy cập tương ứng.
  private final TextWatcher textWatcher = new TextWatcher() {
    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức beforeTextChanged với phạm vi truy cập tương ứng.
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức onTextChanged với phạm vi truy cập tương ứng.
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSendButtonState();.
      updateSendButtonState();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Áp dụng annotation @Override cho phần tử bên dưới.
    @Override
    // Định nghĩa phương thức afterTextChanged với phạm vi truy cập tương ứng.
    public void afterTextChanged(Editable s) { }
  // Thực thi câu lệnh: };.
  };

  // Áp dụng annotation @Nullable cho phần tử bên dưới.
  @Nullable
  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreateView với phạm vi truy cập tương ứng.
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      // Áp dụng annotation @Nullable cho phần tử bên dưới.
      @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = inflater.inflate(R.layout.fragment_chat, container, false);.
    View view = inflater.inflate(R.layout.fragment_chat, container, false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages = view.findViewById(R.id.rvMessages);.
    rvMessages = view.findViewById(R.id.rvMessages);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvConnectionStatus = view.findViewById(R.id.tvConnectionStatus);.
    tvConnectionStatus = view.findViewById(R.id.tvConnectionStatus);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadTitle = view.findViewById(R.id.tvThreadTitle);.
    tvThreadTitle = view.findViewById(R.id.tvThreadTitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadSubtitle = view.findViewById(R.id.tvThreadSubtitle);.
    tvThreadSubtitle = view.findViewById(R.id.tvThreadSubtitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState = view.findViewById(R.id.tvEmptyState);.
    tvEmptyState = view.findViewById(R.id.tvEmptyState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtMessage = view.findViewById(R.id.edtMessage);.
    edtMessage = view.findViewById(R.id.edtMessage);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSend = view.findViewById(R.id.btnSend);.
    btnSend = view.findViewById(R.id.btnSend);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());.
    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: layoutManager.setStackFromEnd(true);.
    layoutManager.setStackFromEnd(true);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setLayoutManager(layoutManager);.
    rvMessages.setLayoutManager(layoutManager);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter = new ChatMessagesAdapter();.
    adapter = new ChatMessagesAdapter();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setAdapter(adapter);.
    rvMessages.setAdapter(adapter);

    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    btnSend.setOnClickListener(v -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (viewModel != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: CharSequence text = edtMessage.getText();.
        CharSequence text = edtMessage.getText();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.sendMessage(text != null ? text.toString() : null);.
        viewModel.sendMessage(text != null ? text.toString() : null);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: edtMessage.setText("");.
        edtMessage.setText("");
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtMessage.addTextChangedListener(textWatcher);.
    edtMessage.addTextChangedListener(textWatcher);

    // Trả về kết quả view;.
    return view;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onViewCreated với phạm vi truy cập tương ứng.
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onViewCreated(view, savedInstanceState);.
    super.onViewCreated(view, savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);.
    viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (adapter != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.setLocalDisplayName(viewModel.getLocalDisplayName());.
      adapter.setLocalDisplayName(viewModel.getLocalDisplayName());
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }

    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);.
    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);.
    viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);.
    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    viewModel.getThreads().observe(getViewLifecycleOwner(), threads -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (threads == null || threads.isEmpty()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
        tvEmptyState.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_empty_state);.
        tvEmptyState.setText(R.string.chat_empty_state);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onStart với phạm vi truy cập tương ứng.
  public void onStart() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onStart();.
    super.onStart();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (viewModel != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.connect();.
      viewModel.connect();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onStop với phạm vi truy cập tương ứng.
  public void onStop() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onStop();.
    super.onStop();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (viewModel != null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.disconnect();.
      viewModel.disconnect();
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onDestroyView với phạm vi truy cập tương ứng.
  public void onDestroyView() {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onDestroyView();.
    super.onDestroyView();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtMessage.removeTextChangedListener(textWatcher);.
    edtMessage.removeTextChangedListener(textWatcher);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setAdapter(null);.
    rvMessages.setAdapter(null);
    // Gán giá trị cho biến hoặc thuộc tính: adapter = null.
    adapter = null;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức renderConnectionState với phạm vi truy cập tương ứng.
  private void renderConnectionState(ChatSocketClient.ConnectionState state) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (tvConnectionStatus == null) return;
    // Thực thi câu lệnh: int textRes;.
    int textRes;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (state == ChatSocketClient.ConnectionState.CONNECTED) {
      // Gán giá trị cho biến hoặc thuộc tính: textRes = R.string.chat_status_connected.
      textRes = R.string.chat_status_connected;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (state == ChatSocketClient.ConnectionState.CONNECTING) {
      // Gán giá trị cho biến hoặc thuộc tính: textRes = R.string.chat_status_connecting.
      textRes = R.string.chat_status_connecting;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else if (state == ChatSocketClient.ConnectionState.FAILED) {
      // Gán giá trị cho biến hoặc thuộc tính: textRes = R.string.chat_status_failed.
      textRes = R.string.chat_status_failed;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Gán giá trị cho biến hoặc thuộc tính: textRes = R.string.chat_status_disconnected.
      textRes = R.string.chat_status_disconnected;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvConnectionStatus.setText(textRes);.
    tvConnectionStatus.setText(textRes);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức renderActiveThread với phạm vi truy cập tương ứng.
  private void renderActiveThread(@Nullable ChatThreadEntity thread) {
    // Gán giá trị cho biến hoặc thuộc tính: hasActiveThread = thread != null.
    hasActiveThread = thread != null;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (thread == null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadTitle.setText(R.string.chat_default_title);.
      tvThreadTitle.setText(R.string.chat_default_title);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadSubtitle.setText(R.string.chat_default_subtitle);.
      tvThreadSubtitle.setText(R.string.chat_default_subtitle);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (adapter != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.setRemoteDisplayName(getString(R.string.chat_default_title));.
        adapter.setRemoteDisplayName(getString(R.string.chat_default_title));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (TextUtils.isEmpty(thread.title)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadTitle.setText(R.string.chat_default_title);.
        tvThreadTitle.setText(R.string.chat_default_title);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadTitle.setText(thread.title);.
        tvThreadTitle.setText(thread.title);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String partnerName = TextUtils.isEmpty(thread.title).
      String partnerName = TextUtils.isEmpty(thread.title)
          // Thực hiện lời gọi phương thức hoặc khởi tạo: ? getString(R.string.chat_default_title).
          ? getString(R.string.chat_default_title)
          // Thực thi câu lệnh: : thread.title;.
          : thread.title;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));.
      tvThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (adapter != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.setRemoteDisplayName(partnerName);.
        adapter.setRemoteDisplayName(partnerName);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSendButtonState();.
    updateSendButtonState();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức renderMessages với phạm vi truy cập tương ứng.
  private void renderMessages(@Nullable List<ChatMessageEntity> messages) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (adapter == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: adapter.submitSafeList(messages);.
    adapter.submitSafeList(messages);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (messages == null || messages.isEmpty()) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
      tvEmptyState.setVisibility(View.VISIBLE);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_empty_state);.
      tvEmptyState.setText(R.string.chat_empty_state);
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.GONE);.
      tvEmptyState.setVisibility(View.GONE);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      rvMessages.post(() -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (adapter != null && adapter.getItemCount() > 0) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.scrollToPosition(adapter.getItemCount() - 1);.
          rvMessages.scrollToPosition(adapter.getItemCount() - 1);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (viewModel != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.markCurrentThreadRead();.
        viewModel.markCurrentThreadRead();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức updateSendButtonState với phạm vi truy cập tương ứng.
  private void updateSendButtonState() {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (btnSend == null) return;
    // Gán giá trị cho biến hoặc thuộc tính: CharSequence text = edtMessage != null ? edtMessage.getText() : null.
    CharSequence text = edtMessage != null ? edtMessage.getText() : null;
    // Gán giá trị cho biến hoặc thuộc tính: boolean hasContent = text != null && text.length() > 0.
    boolean hasContent = text != null && text.length() > 0;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSend.setEnabled(hasActiveThread && hasContent);.
    btnSend.setEnabled(hasActiveThread && hasContent);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
