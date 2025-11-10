# Chức năng của Trí: Giỏ hàng và hiển thị sản phẩm cho khách hàng

## Trang chủ khách hàng (`HomeFragment`)
- Hiển thị danh sách sản phẩm bằng `RecyclerView` và `ProductsAdapter` với thao tác thêm vào giỏ và mở chi tiết.
- Sử dụng `HomeVM` để quan sát danh sách sản phẩm, danh mục và danh mục được chọn.
- Cung cấp chip bộ lọc danh mục, shortcut tới giỏ hàng, đơn hàng và bản đồ cửa hàng.
- Hiển thị lời chào tùy thời điểm trong ngày và tên người dùng từ `SharedPreferences`.
- Ô tìm kiếm và các nút "See all" đang thông báo trạng thái phát triển.

## Giỏ hàng (`CartFragment`)
- Sử dụng `CartVM` để quan sát danh sách giỏ hàng theo thời gian thực.
- `CartAdapter` cho phép tăng/giảm số lượng hoặc xóa sản phẩm khỏi giỏ.
- Tính toán tổng số lượng và tổng tiền, định dạng theo chuẩn VND.
- Nút Checkout gọi `OrdersVM.checkout` và hiển thị kết quả thành công hoặc thất bại.

## Trải nghiệm tổng thể
- Người dùng có thể duyệt sản phẩm, thêm vào giỏ, xem số lượng trực tiếp từ badge.
- Các shortcut giúp chuyển nhanh sang giỏ hàng, lịch sử đơn và vị trí cửa hàng.
