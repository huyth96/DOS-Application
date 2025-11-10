
# DrinkOrder Android App

Ứng dụng đặt nước viết bằng **Java + MVVM + Room*
## 📦 Chức năng
- Đăng ký / Đăng nhập (demo: `user1/123456`)
- Xem danh mục & sản phẩm
- Xem chi tiết sản phẩm
- Giỏ hàng: thêm, xoá, tăng/giảm số lượng
- Thanh toán (giả lập, lưu vào Room DB)
- Lịch sử đơn hàng

## 🏗️ Công nghệ
- **UI**: Activity, Fragment, RecyclerView, Glide  
- **Data**: Room Database + seed từ `assets/seed.json`  
- **MVVM**: ViewModel + LiveData + Repository

## 🌐 Local chat dev server
Vì dự án chưa có backend thực tế, muốn test màn Chat bạn cần chạy WebSocket server giả lập:

1. Cài Node.js ≥ 18 rồi cài thư viện:
   ```bash
   npm install ws
   ```
2. Khởi chạy server:
   ```bash
   node scripts/chat-dev-server.js
   ```
   Có thể thay đổi host/port/path qua biến môi trường, ví dụ:
   ```bash
   CHAT_SERVER_PORT=8080 CHAT_SERVER_PATH=/ws node scripts/chat-dev-server.js
   ```
3. Sửa `app/build.gradle` → `defaultConfig` → `buildConfigField "String", "CHAT_SOCKET_URL", ...` thành URL server mới (ví dụ `ws://10.0.2.2:8080/ws` khi chạy trên Android emulator).
4. Rebuild và cài lại app. Khi mở tab Chat sẽ thấy trạng thái “Connected” và server sẽ echo lại tin nhắn để bạn kiểm tra luồng UI.





## Chat tips (EN)
- Run two sessions (one customer, one admin) to exercise two-way chat; each account maps to 	hread-<userId> so admins can switch between conversations.
- Room DB tables chat_threads and chat_messages persist the full history, so you can close/reopen the app without losing context while the dev server keeps in-memory state.

