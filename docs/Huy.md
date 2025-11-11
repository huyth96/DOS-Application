# Chức năng của Huy: Đăng ký, đăng nhập, quản lý tài khoản

## Luồng đăng nhập (`LoginActivity`)
1. Activity khởi tạo layout `activity_login` và ánh xạ các trường nhập liệu người dùng.
2. `AuthRepository` được khởi tạo với `AppDatabase` và `SharedPreferences` để kiểm tra phiên đăng nhập.
3. Nếu người dùng đã đăng nhập, hệ thống tự động điều hướng sang `MainActivity`.
4. Khi người dùng nhấn **Login**, thông tin được kiểm tra: thiếu dữ liệu sẽ báo bằng `Toast`.
5. Việc xác thực thực thi trong `Thread` phụ, trả về `AuthRepository.LoginStatus`:
   - `SUCCESS`: thông báo thành công và mở `MainActivity`.
   - `BANNED`: thông báo tài khoản bị khóa.
   - Trường hợp khác: báo sai thông tin.

## Luồng đăng ký (`RegisterActivity`)
1. Giao diện `activity_register` thu thập username, mật khẩu, họ tên, email, số điện thoại.
2. `AuthRepository` dùng chung để tự đăng nhập sau khi tạo tài khoản.
3. Thread nền kiểm tra trùng username bằng `UserDao`.
4. Nếu hợp lệ, tạo `UserEntity` với quyền `customer`, lưu thời gian tạo và trạng thái chưa khóa.
5. Tự động đăng nhập và chuyển sang `MainActivity`; nếu thất bại, hiển thị thông báo phù hợp.

## Hồ sơ người dùng (`ProfileActivity`, `EditProfileActivity`)
- `ProfileActivity` tải thông tin người dùng hiện tại từ Room, hiển thị họ tên, email, số điện thoại, vai trò.
- Hỗ trợ đăng xuất bằng cách xóa `SharedPreferences` và quay lại màn đăng nhập.
- Cho phép mở `EditProfileActivity`; khi cập nhật thành công sẽ tải lại dữ liệu.
- `EditProfileActivity` hiển thị sẵn thông tin hồ sơ, cho phép chỉnh sửa và thay đổi mật khẩu.
- Email được kiểm tra định dạng, việc cập nhật thực hiện trong thread phụ thông qua `UserDao`.

## Nhật ký và phiên đăng nhập
- Trạng thái đăng nhập được lưu trong `SharedPreferences` với namespace `auth`.
- `AuthRepository` chịu trách nhiệm đọc/ghi thông tin đăng nhập, hỗ trợ `logout()` để xóa phiên.
