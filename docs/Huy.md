# Tính năng của Huy: Đăng ký, Đăng nhập, Quản lý tài khoản

## Luồng đăng ký (`RegisterActivity`)
- Khởi tạo layout `activity_register`, bind toàn bộ trường nhập và tạo `AuthRepository` dùng lại cho bước auto-login ngay sau khi đăng ký (`app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:41`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:48`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:63`).
- Hàm `doRegister()` đọc dữ liệu từ năm ô nhập, kiểm tra rỗng và báo lỗi sớm bằng `Toast` nếu thiếu username hoặc mật khẩu (`app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:80`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:92`).
- Phần xử lý nặng chạy trong `Thread` riêng: lấy `UserDao`, kiểm tra trùng username, tạo `UserEntity` với role customer, gán timestamp/ cờ `isBanned` rồi insert vào Room (`app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:100`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:103`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:115`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:129`).
- Sau khi insert, activity tự gọi lại `AuthRepository.login()` để tạo session, chuyển sang `MainActivity` khi thành công, hoặc báo lỗi khi tài khoản bị ban hay auto-login thất bại (`app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:136`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:140`).
- `AuthRepository.login()` kiểm tra thông tin với `UserDao`, từ chối nếu sai hoặc bị ban, và ghi `userId/username/role` vào `SharedPreferences` tên `auth` để tái sử dụng trên toàn app (`app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:24`, `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:32`).

## Luồng đăng nhập (`LoginActivity`)
- `LoginActivity` bind hai ô nhập và hai nút hành động, dựng `AuthRepository` với cùng namespace `auth`; nếu `isLoggedIn()` trả về true thì bỏ qua hoàn toàn màn hình login và mở thẳng `MainActivity` (`app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:42`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:58`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:63`).
- Nút **Login** gọi `doLogin()`: lấy username/password, kiểm tra rỗng, sau đó chạy `auth.login()` trong `Thread` riêng để không chặn UI (`app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:83`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:89`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:97`).
- Kết quả trả về qua UI thread: `SUCCESS` hiển thị toast và chuyển `MainActivity`, `BANNED` hiển thị thông báo khóa tài khoản, còn lại báo sai thông tin (`app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:103`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:111`).
- `AuthRepository.logout()` và `isLoggedIn()` dựa vào cùng `SharedPreferences`, đảm bảo mọi Activity có thể kiểm tra/trả session thống nhất (`app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:40`, `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:44`).

## Quản lý tài khoản (Admin)

### Xem danh sách & thống kê
- `AdminUsersFragment` dựng `RecyclerView` với `AdminUsersAdapter`, hiển thị tổng số user và tổng số đang bị ban (`app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:52`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:86`).
- Ô tìm kiếm sử dụng `TextWatcher` để lọc theo tên, username, email hoặc số điện thoại; kết quả rỗng sẽ kích hoạt bộ empty-state riêng cho trường hợp chưa có dữ liệu hoặc không khớp tìm kiếm (`app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:63`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:100`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:133`).
- `AdminUsersAdapter` định dạng từng dòng: render tên, username, role, ngày tạo; đổi màu trạng thái theo `isBanned` và tự vô hiệu hóa nút hành động đối với tài khoản role admin (`app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:51`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:60`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:66`).

### Ban / Unban
- Khi nhấn nút trên từng dòng, adapter gọi callback để `AdminUsersFragment` mở dialog xác nhận với message tùy trạng thái hiện tại (`app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:74`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:147`).
- Người dùng chấp nhận sẽ kích hoạt `updateBanStatus()`, từ đó gọi `AdminUsersVM.setBanStatus()` thực thi trên thread IO và phản hồi lại qua callback để hiển thị toast thành công/lỗi (`app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:161`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersVM.java:39`).
- ViewModel truy cập `UserDao.updateBanStatus()` để đổi cờ `isBanned` trực tiếp trong Room; LiveData `observeAll()` đẩy danh sách cập nhật về UI ngay sau khi transaction hoàn tất (`app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:21`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:24`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersVM.java:32`).

### Lưu ý session
- Mọi hành động xem/band tài khoản dựa trên trạng thái đăng nhập được lưu trong `SharedPreferences auth`; do đó, logout trên bất kỳ màn hình nào chỉ cần gọi `AuthRepository.logout()` để xóa sạch thông tin và buộc quay về `LoginActivity` (`app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:40`).
