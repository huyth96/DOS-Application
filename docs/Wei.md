# Chức năng của Wei: Chat và Map

## Hệ thống chat (`ChatFragment`, `ChatViewModel`)
- `ChatFragment` hiển thị luồng tin nhắn với `RecyclerView` và `ChatMessagesAdapter`.
- Kết nối tới `ChatViewModel` để nhận trạng thái socket, danh sách thread và tin nhắn.
- Tự động cuộn đến tin mới nhất, đánh dấu đã đọc thread hiện hành và kiểm soát nút gửi.
- `ChatViewModel` xử lý gửi tin, đồng bộ tên hiển thị người dùng và quản lý kết nối thông qua `ChatSocketClient`.

## Bản đồ (`MapActivity`)
- Hiển thị Google Map với marker vị trí cửa hàng mặc định tại trung tâm Q.1.
- Cho phép mở Google Maps bên ngoài thông qua Intent `geo:` và ứng dụng `com.google.android.apps.maps`.
- Sẵn sàng tùy chỉnh các option UI như toolbar, zoom control và location (có thể bật khi cấp quyền).

## Trải nghiệm người dùng
- Chat: người dùng thấy trạng thái kết nối, tiêu đề cuộc trò chuyện và tin nhắn realtime.
- Map: người dùng xem nhanh vị trí cửa hàng và điều hướng ra ứng dụng bản đồ để tới địa điểm.
