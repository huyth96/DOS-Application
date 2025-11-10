# Chức năng của Lộc: Quản lý Order và Profile

## Danh sách đơn hàng (`OrdersFragment`)
- Fragment sử dụng layout `simple_recycler` để hiển thị danh sách đơn.
- `OrdersAdapter` mở chi tiết đơn qua `OrderNavigator` khi người dùng chọn.
- `OrderListVM` lấy dữ liệu từ `OrderDao.getAllOrders()` và phát qua LiveData để cập nhật UI.

## Chi tiết đơn hàng (`OrderDetailFragment`)
- Nhận `orderId` qua `Bundle` và yêu cầu `OrderDetailVM` tải dữ liệu.
- Hiển thị mã đơn, trạng thái, thời gian, tổng tiền và danh sách line item.
- Tự tính tổng tiền nếu entity không cung cấp trường tổng.
- Sử dụng helpers phản xạ để tương thích nhiều kiểu entity (tên trường khác nhau).

## Hồ sơ người dùng (`ProfileActivity`, `EditProfileActivity`)
- Trang hồ sơ hiển thị thông tin người dùng, hỗ trợ đăng xuất và mở màn chỉnh sửa.
- `EditProfileActivity` cho phép cập nhật họ tên, email, số điện thoại, mật khẩu và lưu lại qua `UserDao`.
- Cả hai màn hình đều chạy truy vấn trong thread phụ để tránh block UI.

## Dòng chảy chung
1. Người dùng mở màn hình Orders để xem danh sách đơn và chọn đơn cần xem.
2. `OrderDetailFragment` hiển thị đầy đủ thông tin kèm line item chi tiết.
3. Người dùng có thể quay lại hồ sơ để cập nhật thông tin cá nhân, đồng bộ với dữ liệu đơn hàng.
