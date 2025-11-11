# Luồng Đăng ký, Đăng nhập và Quản lý tài khoản (ban/unban)

Tài liệu này mô tả chi tiết các luồng liên quan đến tài khoản: đăng ký, đăng nhập, quản lý hồ sơ và quản trị ban/unban. Các trích dẫn kèm file và vị trí giúp bạn tra cứu nhanh trong codebase.

## Dữ liệu người dùng (Room)
- Cấu trúc bảng users: `app/src/main/java/com/drinkorder/data/db/entity/UserEntity.java:6`
  - Khóa chính `userId`, trường `username` duy nhất, `passwordHash`, `fullName`, `email`, `phone`, `role`, `createdAt`, `isBanned`.
- DAO thao tác: `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:13`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:17`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:25`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:29`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:35`, `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:39`
  - Tìm user theo username, thêm user, quan sát danh sách, cập nhật ban/unban, cập nhật hồ sơ và mật khẩu.

## Repository xác thực
- AuthRepository chịu trách nhiệm xác thực và lưu phiên: `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:15`
  - Khởi tạo: `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:35`
  - Đăng nhập (kiểm tra Room, từ chối khi banned, lưu SharedPreferences): `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:46`
  - Đăng xuất: `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:63`
  - Kiểm tra đã đăng nhập: `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:68`
  - Lấy username/role hiện tại: `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:78`, `app/src/main/java/com/drinkorder/data/repo/AuthRepository.java:83`

## Luồng Đăng ký (Register)
- Màn hình và binding: `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:25`, `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:31`
- Xử lý register: `app/src/main/java/com/drinkorder/ui/login/RegisterActivity.java:63`
  - Validate cơ bản (username/password bắt buộc, email hợp lệ nếu có)
  - Kiểm tra trùng username trong Room
  - Tạo UserEntity (role=customer, isBanned=false, createdAt=now) và insert
  - Tự động đăng nhập và chuyển sang MainActivity khi thành công

## Luồng Đăng nhập (Login)
- Màn hình và binding: `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:23`, `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:33`
- Nếu đã có phiên đăng nhập thì bỏ qua login: `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:43`
- Đăng nhập nền và phản hồi UI: `app/src/main/java/com/drinkorder/ui/login/LoginActivity.java:67`
  - SUCCESS: vào MainActivity, BANNED: thông báo bị khóa, còn lại: sai thông tin.

## Hồ sơ và đăng xuất
- Xem hồ sơ, logout, đi tới chỉnh sửa: `app/src/main/java/com/drinkorder/ui/login/ProfileActivity.java:23`, `app/src/main/java/com/drinkorder/ui/login/ProfileActivity.java:31`
- Tải thông tin người dùng hiện tại: `app/src/main/java/com/drinkorder/ui/login/ProfileActivity.java:68`
- Chỉnh sửa hồ sơ, đổi mật khẩu: `app/src/main/java/com/drinkorder/ui/login/EditProfileActivity.java:22`, `app/src/main/java/com/drinkorder/ui/login/EditProfileActivity.java:27`

## Quản trị: Ban / Unban
- Danh sách người dùng + thống kê: `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:54`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:94`
- Tìm kiếm, empty-state: `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:109`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:144`
- Xác nhận ban/unban và thực thi: `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:159`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersFragment.java:174`
- ViewModel cập nhật Room trên IO thread: `app/src/main/java/com/drinkorder/ui/admin/AdminUsersVM.java:45`
- Adapter hiển thị trạng thái và bảo vệ tài khoản admin: `app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:55`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:112`, `app/src/main/java/com/drinkorder/ui/admin/AdminUsersAdapter.java:120`
- Truy vấn cập nhật ban/unban: `app/src/main/java/com/drinkorder/data/db/dao/UserDao.java:29`

## Vai trò và điều hướng
- Quyết định giao diện theo role trong phiên: `app/src/main/java/com/drinkorder/ui/MainActivity.java:42`
  - Admin dùng Navigation Drawer với các màn quản trị, Customer dùng Bottom Navigation.

## Lưu ý bảo mật (cần làm khi triển khai thật)
- Không lưu mật khẩu dạng plaintext. Cần dùng hash có salt (vd: bcrypt/argon2) và chỉ so khớp hash.
- Xem xét mã hóa/bảo vệ SharedPreferences nếu lưu thông tin nhạy cảm.
- Nếu có backend, chuyển sang xác thực qua API và JWT, không để logic xác thực 100% trên client.

## Kiểm thử nhanh các luồng
- Đăng ký tài khoản mới và kiểm tra auto-login sang MainActivity.
- Đăng xuất từ Profile và quay lại Login.
- Đăng nhập bằng tài khoản bị ban (qua Admin) để xác nhận hiển thị thông báo bị khóa.
- Tìm kiếm trong danh sách Admin và ban/unban; kiểm tra LiveData cập nhật realtime.
