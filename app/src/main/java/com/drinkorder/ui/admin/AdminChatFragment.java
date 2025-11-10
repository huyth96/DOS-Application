// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

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
// Import com.drinkorder.ui.chat.ChatMessagesAdapter để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.chat.ChatMessagesAdapter;
// Import com.drinkorder.ui.chat.ChatThreadsAdapter để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.chat.ChatThreadsAdapter;
// Import com.drinkorder.ui.chat.ChatViewModel để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.ui.chat.ChatViewModel;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;

// Định nghĩa lớp AdminChatFragment kế thừa Fragment.
public class AdminChatFragment extends Fragment {

  // Khai báo thuộc tính với phạm vi truy cập: private ChatViewModel viewModel.
  private ChatViewModel viewModel;
  // Khai báo thuộc tính với phạm vi truy cập: private ChatThreadsAdapter threadsAdapter.
  private ChatThreadsAdapter threadsAdapter;
  // Khai báo thuộc tính với phạm vi truy cập: private ChatMessagesAdapter messagesAdapter.
  private ChatMessagesAdapter messagesAdapter;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView rvThreads.
  private RecyclerView rvThreads;
  // Khai báo thuộc tính với phạm vi truy cập: private RecyclerView rvMessages.
  private RecyclerView rvMessages;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvConnectionStatus.
  private TextView tvConnectionStatus;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvSelectedThreadTitle.
  private TextView tvSelectedThreadTitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvSelectedThreadSubtitle.
  private TextView tvSelectedThreadSubtitle;
  // Khai báo thuộc tính với phạm vi truy cập: private TextView tvEmptyState.
  private TextView tvEmptyState;
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtMessage.
  private TextInputEditText edtMessage;
  // Khai báo thuộc tính với phạm vi truy cập: private ImageButton btnSend.
  private ImageButton btnSend;
  // Khai báo thuộc tính với phạm vi truy cập: private boolean hasActiveThread = false.
  private boolean hasActiveThread = false;
  // Khai báo thuộc tính với phạm vi truy cập: private boolean hasAnyThread = false.
  private boolean hasAnyThread = false;

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
    // Thực hiện lời gọi phương thức hoặc khởi tạo: View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);.
    View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvThreads = view.findViewById(R.id.rvThreads);.
    rvThreads = view.findViewById(R.id.rvThreads);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages = view.findViewById(R.id.rvAdminMessages);.
    rvMessages = view.findViewById(R.id.rvAdminMessages);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvConnectionStatus = view.findViewById(R.id.tvAdminConnectionStatus);.
    tvConnectionStatus = view.findViewById(R.id.tvAdminConnectionStatus);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadTitle = view.findViewById(R.id.tvSelectedThreadTitle);.
    tvSelectedThreadTitle = view.findViewById(R.id.tvSelectedThreadTitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadSubtitle = view.findViewById(R.id.tvSelectedThreadSubtitle);.
    tvSelectedThreadSubtitle = view.findViewById(R.id.tvSelectedThreadSubtitle);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState = view.findViewById(R.id.tvAdminEmptyState);.
    tvEmptyState = view.findViewById(R.id.tvAdminEmptyState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtMessage = view.findViewById(R.id.edtAdminMessage);.
    edtMessage = view.findViewById(R.id.edtAdminMessage);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSend = view.findViewById(R.id.btnAdminSend);.
    btnSend = view.findViewById(R.id.btnAdminSend);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvThreads.setLayoutManager(new LinearLayoutManager(requireContext()));.
    rvThreads.setLayoutManager(new LinearLayoutManager(requireContext()));
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    threadsAdapter = new ChatThreadsAdapter(thread -> {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (viewModel != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.selectThread(thread.threadId);.
        viewModel.selectThread(thread.threadId);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvThreads.setAdapter(threadsAdapter);.
    rvThreads.setAdapter(threadsAdapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: LinearLayoutManager messagesLayoutManager = new LinearLayoutManager(requireContext());.
    LinearLayoutManager messagesLayoutManager = new LinearLayoutManager(requireContext());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesLayoutManager.setStackFromEnd(true);.
    messagesLayoutManager.setStackFromEnd(true);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setLayoutManager(messagesLayoutManager);.
    rvMessages.setLayoutManager(messagesLayoutManager);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesAdapter = new ChatMessagesAdapter();.
    messagesAdapter = new ChatMessagesAdapter();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setAdapter(messagesAdapter);.
    rvMessages.setAdapter(messagesAdapter);

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
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesAdapter.setLocalDisplayName(viewModel.getLocalDisplayName());.
    messagesAdapter.setLocalDisplayName(viewModel.getLocalDisplayName());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsAdapter.setLocalRole(viewModel.getSenderRole());.
    threadsAdapter.setLocalRole(viewModel.getSenderRole());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);.
    viewModel.getConnectionState().observe(getViewLifecycleOwner(), this::renderConnectionState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getThreads().observe(getViewLifecycleOwner(), this::renderThreads);.
    viewModel.getThreads().observe(getViewLifecycleOwner(), this::renderThreads);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    viewModel.getActiveThreadId().observe(getViewLifecycleOwner(), id -> {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsAdapter.setSelectedThreadId(id);.
      threadsAdapter.setSelectedThreadId(id);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: hasActiveThread = !TextUtils.isEmpty(id);.
      hasActiveThread = !TextUtils.isEmpty(id);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSendButtonState();.
      updateSendButtonState();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);.
    viewModel.getActiveThread().observe(getViewLifecycleOwner(), this::renderActiveThread);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);.
    viewModel.getMessages().observe(getViewLifecycleOwner(), this::renderMessages);
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
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvThreads.setAdapter(null);.
    rvThreads.setAdapter(null);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.setAdapter(null);.
    rvMessages.setAdapter(null);
    // Gán giá trị cho biến hoặc thuộc tính: threadsAdapter = null.
    threadsAdapter = null;
    // Gán giá trị cho biến hoặc thuộc tính: messagesAdapter = null.
    messagesAdapter = null;
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

  // Định nghĩa phương thức renderThreads với phạm vi truy cập tương ứng.
  private void renderThreads(@Nullable List<ChatThreadEntity> threads) {
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (threadsAdapter != null) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (threads == null || threads.isEmpty()) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsAdapter.submitList(java.util.Collections.emptyList());.
        threadsAdapter.submitList(java.util.Collections.emptyList());
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsAdapter.submitList(new java.util.ArrayList<>(threads));.
        threadsAdapter.submitList(new java.util.ArrayList<>(threads));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: hasAnyThread = threads != null && !threads.isEmpty();.
    hasAnyThread = threads != null && !threads.isEmpty();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (!hasAnyThread) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (threadsAdapter != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: threadsAdapter.setSelectedThreadId(null);.
        threadsAdapter.setSelectedThreadId(null);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
      tvEmptyState.setVisibility(View.VISIBLE);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_no_threads);.
      tvEmptyState.setText(R.string.chat_no_threads);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);.
      tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);
      // Gán giá trị cho biến hoặc thuộc tính: hasActiveThread = false.
      hasActiveThread = false;
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.GONE);.
      tvEmptyState.setVisibility(View.GONE);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: updateSendButtonState();.
    updateSendButtonState();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức renderActiveThread với phạm vi truy cập tương ứng.
  private void renderActiveThread(@Nullable ChatThreadEntity thread) {
    // Gán giá trị cho biến hoặc thuộc tính: hasActiveThread = thread != null.
    hasActiveThread = thread != null;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (thread == null) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadTitle.setText(R.string.chat_default_title);.
      tvSelectedThreadTitle.setText(R.string.chat_default_title);
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);.
      tvSelectedThreadSubtitle.setText(R.string.chat_select_thread);
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (messagesAdapter != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesAdapter.setRemoteDisplayName(getString(R.string.chat_default_title));.
        messagesAdapter.setRemoteDisplayName(getString(R.string.chat_default_title));
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (TextUtils.isEmpty(thread.title)) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadTitle.setText(R.string.chat_default_title);.
        tvSelectedThreadTitle.setText(R.string.chat_default_title);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadTitle.setText(thread.title);.
        tvSelectedThreadTitle.setText(thread.title);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: String partnerName = TextUtils.isEmpty(thread.title).
      String partnerName = TextUtils.isEmpty(thread.title)
          // Thực hiện lời gọi phương thức hoặc khởi tạo: ? getString(R.string.chat_default_title).
          ? getString(R.string.chat_default_title)
          // Thực thi câu lệnh: : thread.title;.
          : thread.title;
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvSelectedThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));.
      tvSelectedThreadSubtitle.setText(getString(R.string.chat_thread_with, partnerName));
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (messagesAdapter != null) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesAdapter.setRemoteDisplayName(partnerName);.
        messagesAdapter.setRemoteDisplayName(partnerName);
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
    if (messagesAdapter == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: messagesAdapter.submitSafeList(messages);.
    messagesAdapter.submitSafeList(messages);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (messages == null || messages.isEmpty()) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (!hasAnyThread) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
        tvEmptyState.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_no_threads);.
        tvEmptyState.setText(R.string.chat_no_threads);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else if (!hasActiveThread) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
        tvEmptyState.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_select_thread);.
        tvEmptyState.setText(R.string.chat_select_thread);
      // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
      } else {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.VISIBLE);.
        tvEmptyState.setVisibility(View.VISIBLE);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setText(R.string.chat_empty_state);.
        tvEmptyState.setText(R.string.chat_empty_state);
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Kết thúc nhánh trước và bắt đầu nhánh else cho cấu trúc điều kiện.
    } else {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: tvEmptyState.setVisibility(View.GONE);.
      tvEmptyState.setVisibility(View.GONE);
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      rvMessages.post(() -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (messagesAdapter != null && messagesAdapter.getItemCount() > 0) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: rvMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);.
          rvMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
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
