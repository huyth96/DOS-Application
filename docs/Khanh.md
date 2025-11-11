# Chức năng của Khanh: Quản lý Category và Product

## Danh mục (`AdminCategoriesFragment`)
- Fragment hiển thị danh sách danh mục với `RecyclerView` và `AdminCategoriesAdapter`.
- Hỗ trợ tìm kiếm theo tên/mô tả bằng `TextWatcher`; kết quả lọc cập nhật empty state phù hợp.
- Cho phép tạo/sửa danh mục qua `AdminCategoryDialog` và ViewModel `AdminCategoriesVM`.
- Xóa danh mục với hộp thoại xác nhận `MaterialAlertDialogBuilder`.
- Giao diện hiển thị tổng số danh mục và thời gian cập nhật gần nhất.

## Sản phẩm (`AdminProductsFragment`)
- Fragment hiển thị danh sách sản phẩm và thống kê số lượng sản phẩm, danh mục.
- Bộ lọc tìm kiếm theo tên sản phẩm hoặc tên danh mục liên kết.
- Cho phép mở form chỉnh sửa (`AdminProductFormActivity`) hoặc xóa sản phẩm với xác nhận.
- Quan sát `AdminProductsVM` để cập nhật dữ liệu sản phẩm và danh mục, đồng bộ tên danh mục trong adapter.
- Empty state mô tả rõ ràng khi danh sách trống hoặc lọc không ra kết quả.

## Quy trình làm việc tổng quát
1. ViewModel tải dữ liệu từ Room và phát qua LiveData.
2. Fragment quan sát LiveData, cập nhật danh sách và thông tin thống kê.
3. Các hành động thêm/sửa/xóa dùng callback của ViewModel để phản hồi thành công hoặc lỗi.
