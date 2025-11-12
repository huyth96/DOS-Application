package com.drinkorder.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.drinkorder.R;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminProductFormActivity extends AppCompatActivity { // Định nghĩa lớp AdminProductFormActivity kế thừa từ AppCompatActivity, dùng để quản lý form thêm/sửa sản phẩm.

  private static final String EXTRA_PRODUCT_ID = "extra_product_id"; // Hằng số tĩnh lưu key cho extra trong Intent, dùng để truyền ID sản phẩm đang chỉnh sửa.

  public static void start(Context context, int productId){ // Phương thức tĩnh để khởi động activity này, nhận context và ID sản phẩm.
    Intent i = new Intent(context, AdminProductFormActivity.class); // Tạo Intent mới để khởi động chính lớp này.
    i.putExtra(EXTRA_PRODUCT_ID, productId); // Thêm extra vào Intent với key EXTRA_PRODUCT_ID và giá trị productId.
    context.startActivity(i); // Khởi động activity từ context đã cung cấp.
  }

  private AdminProductFormVM vm; // Biến instance lưu ViewModel cho form này, dùng để xử lý logic dữ liệu.
  private TextInputEditText edtName, edtDescription, edtPrice, edtRating, edtStock, edtImage; // Các biến lưu tham chiếu đến các trường input cho tên, mô tả, giá, đánh giá, tồn kho và hình ảnh.
  private Spinner spinnerCategory; // Biến lưu Spinner để chọn danh mục sản phẩm.
  private ImageView imgPreview; // Biến lưu ImageView để hiển thị preview hình ảnh sản phẩm.
  private MaterialButton btnSave; // Biến lưu nút lưu sản phẩm.
  private final List<CategoryEntity> categoryData = new ArrayList<>(); // Danh sách lưu dữ liệu các danh mục, khởi tạo là ArrayList rỗng.
  private ArrayAdapter<String> categoryAdapter; // Adapter cho Spinner, hiển thị tên các danh mục.
  private int editingProductId = -1; // Biến lưu ID sản phẩm đang chỉnh sửa, mặc định -1 nghĩa là thêm mới.
  private int pendingCategoryId = -1; // Biến lưu ID danh mục đang chờ để chọn, dùng khi dữ liệu danh mục chưa tải xong.
  private String selectedImageSource; // Biến lưu nguồn hình ảnh đã chọn (URI hoặc URL).

  private final ActivityResultLauncher<String> imagePicker = // Biến lưu launcher để chọn hình ảnh từ gallery.
          registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> { // Đăng ký callback cho kết quả chọn hình ảnh.
            if (uri != null) { // Kiểm tra nếu URI không null (nghĩa là đã chọn hình ảnh).
              selectedImageSource = uri.toString(); // Lưu URI dưới dạng string vào selectedImageSource.
              edtImage.setText(selectedImageSource); // Đặt text cho trường input hình ảnh.
              loadPreview(selectedImageSource); // Tải preview hình ảnh từ nguồn đã chọn.
            }
          });

  @Override // Ghi đè phương thức onCreate từ AppCompatActivity.
  protected void onCreate(@Nullable Bundle savedInstanceState) { // Phương thức khởi tạo activity, nhận savedInstanceState có thể null.
    super.onCreate(savedInstanceState); // Gọi onCreate của lớp cha.
    setContentView(R.layout.activity_admin_product_form); // Đặt layout cho activity từ resource R.layout.activity_admin_product_form.

    MaterialToolbar toolbar = findViewById(R.id.toolbarForm); // Tìm và lưu toolbar từ layout bằng ID.
    toolbar.setNavigationOnClickListener(v -> finish()); // Thiết lập listener cho nút navigation để kết thúc activity.

    edtName = findViewById(R.id.edtName); // Tìm và lưu trường input tên sản phẩm.
    edtDescription = findViewById(R.id.edtDescription); // Tìm và lưu trường input mô tả.
    edtPrice = findViewById(R.id.edtPrice); // Tìm và lưu trường input giá.
    edtRating = findViewById(R.id.edtRating); // Tìm và lưu trường input đánh giá.
    edtStock = findViewById(R.id.edtStock); // Tìm và lưu trường input tồn kho.
    edtImage = findViewById(R.id.edtImageUrl); // Tìm và lưu trường input URL hình ảnh.
    spinnerCategory = findViewById(R.id.spinnerCategory); // Tìm và lưu Spinner danh mục.
    imgPreview = findViewById(R.id.imgPreview); // Tìm và lưu ImageView preview.
    MaterialButton btnPickImage = findViewById(R.id.btnPickImage); // Tìm và lưu nút chọn hình ảnh.
    btnSave = findViewById(R.id.btnSaveProduct); // Tìm và lưu nút lưu sản phẩm.

    categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>()); // Khởi tạo adapter cho Spinner với layout mặc định và danh sách rỗng.
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Thiết lập layout cho dropdown của Spinner.
    spinnerCategory.setAdapter(categoryAdapter); // Gán adapter cho Spinner.

    btnPickImage.setOnClickListener(v -> imagePicker.launch("image/*")); // Thiết lập listener cho nút chọn hình ảnh, khởi động launcher với loại image/*.

    btnSave.setOnClickListener(v -> saveProduct()); // Thiết lập listener cho nút lưu, gọi phương thức saveProduct.

    editingProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1); // Lấy ID sản phẩm từ Intent, mặc định -1 nếu không có.
    toolbar.setTitle(editingProductId > 0 ? "Edit product" : "Add product"); // Đặt tiêu đề toolbar dựa trên việc chỉnh sửa hay thêm mới.

    vm = new ViewModelProvider(this).get(AdminProductFormVM.class); // Khởi tạo ViewModel từ ViewModelProvider.
    vm.categories().observe(this, this::applyCategories); // Quan sát LiveData danh mục và gọi applyCategories khi thay đổi.
    if (editingProductId > 0) { // Nếu đang chỉnh sửa (ID > 0).
      vm.product(editingProductId).observe(this, this::bindProduct); // Quan sát LiveData sản phẩm và gọi bindProduct khi thay đổi.
    }
  }

  private void applyCategories(List<CategoryEntity> list){ // Phương thức áp dụng danh sách danh mục vào adapter.
    categoryData.clear(); // Xóa dữ liệu danh mục hiện tại.
    if (list != null) categoryData.addAll(list); // Thêm tất cả danh mục mới nếu list không null.
    List<String> names = new ArrayList<>(); // Tạo danh sách tên danh mục.
    for (CategoryEntity c : categoryData) { // Duyệt qua từng danh mục.
      names.add(c.name); // Thêm tên vào danh sách.
    }
    categoryAdapter.clear(); // Xóa dữ liệu adapter.
    categoryAdapter.addAll(names); // Thêm tất cả tên vào adapter.
    categoryAdapter.notifyDataSetChanged(); // Thông báo adapter thay đổi dữ liệu.
    if (pendingCategoryId > 0) { // Nếu có ID danh mục chờ.
      selectCategory(pendingCategoryId); // Chọn danh mục đó.
    }
  }

  private void bindProduct(ProductEntity product){ // Phương thức bind dữ liệu sản phẩm vào các trường input.
    if (product == null) return; // Thoát nếu sản phẩm null.
    edtName.setText(product.name); // Đặt tên sản phẩm.
    edtDescription.setText(product.description); // Đặt mô tả.
    edtPrice.setText(String.format(Locale.getDefault(), "%.0f", product.price)); // Đặt giá với định dạng không thập phân.
    edtRating.setText(product.rating == null ? "" : String.format(Locale.getDefault(), "%.1f", product.rating)); // Đặt đánh giá nếu có, định dạng 1 thập phân.
    edtStock.setText(product.stock == null ? "" : String.valueOf(product.stock)); // Đặt tồn kho nếu có.
    edtImage.setText(product.imageUrl); // Đặt URL hình ảnh.
    selectedImageSource = product.imageUrl; // Lưu nguồn hình ảnh.
    loadPreview(selectedImageSource); // Tải preview.
    pendingCategoryId = product.categoryId; // Lưu ID danh mục chờ.
    selectCategory(product.categoryId); // Chọn danh mục.
  }

  private void selectCategory(int categoryId){ // Phương thức chọn danh mục trong Spinner dựa trên ID.
    if (categoryData.isEmpty()) return; // Thoát nếu danh sách rỗng.
    for (int i = 0; i < categoryData.size(); i++) { // Duyệt qua danh sách.
      if (categoryData.get(i).categoryId == categoryId) { // Nếu tìm thấy ID khớp.
        spinnerCategory.setSelection(i); // Chọn vị trí đó.
        break; // Thoát vòng lặp.
      }
    }
  }

  private void saveProduct(){ // Phương thức lưu sản phẩm.
    String name = textOf(edtName); // Lấy tên từ input.
    String desc = textOf(edtDescription); // Lấy mô tả.
    double price = parseDouble(textOf(edtPrice)); // Parse giá.
    double rating = parseDouble(textOf(edtRating)); // Parse đánh giá.
    int stock = parseInt(textOf(edtStock)); // Parse tồn kho.
    int categoryId = getSelectedCategoryId(); // Lấy ID danh mục đã chọn.
    selectedImageSource = textOf(edtImage); // Lấy nguồn hình ảnh.

    if (TextUtils.isEmpty(name)) { // Kiểm tra tên rỗng.
      edtName.setError("Name cannot be empty"); // Hiển thị lỗi.
      return; // Thoát.
    }
    if (price <= 0) { // Kiểm tra giá <= 0.
      edtPrice.setError("Price must be greater than 0"); // Hiển thị lỗi.
      return; // Thoát.
    }
    if (categoryId <= 0) { // Kiểm tra danh mục không hợp lệ.
      Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show(); // Hiển thị toast.
      return; // Thoát.
    }
    if (rating <= 0) rating = 4.5; // Đặt mặc định đánh giá nếu <= 0.
    if (rating > 5) rating = 5.0; // Giới hạn đánh giá tối đa 5.
    if (stock < 0) stock = 0; // Đặt tồn kho tối thiểu 0.

    ProductEntity product = new ProductEntity(); // Tạo entity sản phẩm mới.
    product.productId = editingProductId > 0 ? editingProductId : 0; // Đặt ID, 0 nếu thêm mới.
    product.name = name.trim(); // Đặt tên đã trim.
    product.description = desc.trim(); // Đặt mô tả đã trim.
    product.price = price; // Đặt giá.
    product.rating = rating; // Đặt đánh giá.
    product.stock = stock; // Đặt tồn kho.
    product.categoryId = categoryId; // Đặt ID danh mục.
    product.imageUrl = selectedImageSource; // Đặt URL hình ảnh.

    btnSave.setEnabled(false); // Vô hiệu hóa nút lưu để tránh click nhiều lần.
    vm.save(product, new AdminProductFormVM.SaveCallback() { // Gọi save từ ViewModel với callback.
      @Override public void onSuccess(int productId) { // Callback thành công.
        Toast.makeText(AdminProductFormActivity.this, "Product saved", Toast.LENGTH_SHORT).show(); // Hiển thị toast thành công.
        setResult(RESULT_OK); // Đặt kết quả activity là OK.
        finish(); // Kết thúc activity.
      }

      @Override public void onError(Throwable throwable) { // Callback lỗi.
        btnSave.setEnabled(true); // Kích hoạt lại nút lưu.
        Toast.makeText(AdminProductFormActivity.this, "Save failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show(); // Hiển thị toast lỗi với message.
      }
    });
  }

  private int getSelectedCategoryId(){ // Phương thức lấy ID danh mục đã chọn từ Spinner.
    int position = spinnerCategory.getSelectedItemPosition(); // Lấy vị trí đã chọn.
    if (position < 0 || position >= categoryData.size()) return -1; // Trả về -1 nếu vị trí không hợp lệ.
    return categoryData.get(position).categoryId; // Trả về ID từ danh sách.
  }

  private String textOf(TextInputEditText edt){ // Phương thức lấy text từ TextInputEditText, trim và xử lý null.
    return edt.getText() == null ? "" : edt.getText().toString().trim(); // Trả về string rỗng nếu null, иначе trim text.
  }

  private double parseDouble(String value){ // Phương thức parse double từ string, xử lý lỗi.
    try { return Double.parseDouble(value); } // Thử parse.
    catch (NumberFormatException e){ return 0d; } // Trả về 0 nếu lỗi.
  }

  private int parseInt(String value){ // Phương thức parse int từ string, xử lý lỗi.
    try { return Integer.parseInt(value); } // Thử parse.
    catch (NumberFormatException e){ return 0; } // Trả về 0 nếu lỗi.
  }

  private void loadPreview(String source){ // Phương thức tải preview hình ảnh vào ImageView.
    Object resolved = resolveImageSource(source); // Resolve nguồn hình ảnh thành object phù hợp.
    Glide.with(this) // Sử dụng Glide để load hình ảnh.
            .load(resolved) // Load từ resolved source.
            .apply(new RequestOptions().transform(new CenterCrop()).placeholder(R.drawable.bg_app_gradient).error(R.drawable.bg_app_gradient)) // Áp dụng options: crop center, placeholder và error.
            .into(imgPreview); // Load vào imgPreview.
  }

  private Object resolveImageSource(String source){ // Phương thức resolve nguồn hình ảnh thành URI hoặc string.
    if (TextUtils.isEmpty(source)) { // Nếu nguồn rỗng.
      return R.drawable.bg_app_gradient; // Trả về drawable mặc định.
    }
    if (source.startsWith("content://") || source.startsWith("file://")) { // Nếu là URI content hoặc file.
      return Uri.parse(source); // Parse thành URI.
    }
    return source; // Trả về string nếu là URL.
  }
}
