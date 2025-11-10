// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.content.Context để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Context;
// Import android.content.Intent để sử dụng các lớp hoặc hàm tương ứng.
import android.content.Intent;
// Import android.net.Uri để sử dụng các lớp hoặc hàm tương ứng.
import android.net.Uri;
// Import android.os.Bundle để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Bundle;
// Import android.text.TextUtils để sử dụng các lớp hoặc hàm tương ứng.
import android.text.TextUtils;
// Import android.widget.ArrayAdapter để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.ArrayAdapter;
// Import android.widget.ImageView để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.ImageView;
// Import android.widget.Spinner để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Spinner;
// Import android.widget.Toast để sử dụng các lớp hoặc hàm tương ứng.
import android.widget.Toast;

// Import androidx.activity.result.ActivityResultLauncher để sử dụng các lớp hoặc hàm tương ứng.
import androidx.activity.result.ActivityResultLauncher;
// Import androidx.activity.result.contract.ActivityResultContracts để sử dụng các lớp hoặc hàm tương ứng.
import androidx.activity.result.contract.ActivityResultContracts;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.appcompat.app.AppCompatActivity để sử dụng các lớp hoặc hàm tương ứng.
import androidx.appcompat.app.AppCompatActivity;
// Import androidx.lifecycle.ViewModelProvider để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.ViewModelProvider;

// Import com.bumptech.glide.Glide để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.Glide;
// Import com.bumptech.glide.load.resource.bitmap.CenterCrop để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
// Import com.bumptech.glide.request.RequestOptions để sử dụng các lớp hoặc hàm tương ứng.
import com.bumptech.glide.request.RequestOptions;
// Import com.drinkorder.R để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.R;
// Import com.drinkorder.data.db.entity.CategoryEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.CategoryEntity;
// Import com.drinkorder.data.db.entity.ProductEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.ProductEntity;
// Import com.google.android.material.appbar.MaterialToolbar để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.appbar.MaterialToolbar;
// Import com.google.android.material.button.MaterialButton để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.button.MaterialButton;
// Import com.google.android.material.textfield.TextInputEditText để sử dụng các lớp hoặc hàm tương ứng.
import com.google.android.material.textfield.TextInputEditText;

// Import java.util.ArrayList để sử dụng các lớp hoặc hàm tương ứng.
import java.util.ArrayList;
// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.Locale để sử dụng các lớp hoặc hàm tương ứng.
import java.util.Locale;

// Định nghĩa lớp AdminProductFormActivity kế thừa AppCompatActivity.
public class AdminProductFormActivity extends AppCompatActivity {

  // Khai báo thuộc tính với phạm vi truy cập: private static final String EXTRA_PRODUCT_ID = "extra_product_id".
  private static final String EXTRA_PRODUCT_ID = "extra_product_id";

  // Định nghĩa phương thức start với phạm vi truy cập tương ứng.
  public static void start(Context context, int productId){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Intent i = new Intent(context, AdminProductFormActivity.class);.
    Intent i = new Intent(context, AdminProductFormActivity.class);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: i.putExtra(EXTRA_PRODUCT_ID, productId);.
    i.putExtra(EXTRA_PRODUCT_ID, productId);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: context.startActivity(i);.
    context.startActivity(i);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private AdminProductFormVM vm.
  private AdminProductFormVM vm;
  // Khai báo thuộc tính với phạm vi truy cập: private TextInputEditText edtName, edtDescription, edtPrice, edtRating, edtStock, edtImage.
  private TextInputEditText edtName, edtDescription, edtPrice, edtRating, edtStock, edtImage;
  // Khai báo thuộc tính với phạm vi truy cập: private Spinner spinnerCategory.
  private Spinner spinnerCategory;
  // Khai báo thuộc tính với phạm vi truy cập: private ImageView imgPreview.
  private ImageView imgPreview;
  // Khai báo thuộc tính với phạm vi truy cập: private MaterialButton btnSave.
  private MaterialButton btnSave;
  // Khai báo thuộc tính với phạm vi truy cập: private final List<CategoryEntity> categoryData = new ArrayList<>().
  private final List<CategoryEntity> categoryData = new ArrayList<>();
  // Khai báo thuộc tính với phạm vi truy cập: private ArrayAdapter<String> categoryAdapter.
  private ArrayAdapter<String> categoryAdapter;
  // Khai báo thuộc tính với phạm vi truy cập: private int editingProductId = -1.
  private int editingProductId = -1;
  // Khai báo thuộc tính với phạm vi truy cập: private int pendingCategoryId = -1.
  private int pendingCategoryId = -1;
  // Khai báo thuộc tính với phạm vi truy cập: private String selectedImageSource.
  private String selectedImageSource;

  // Thực thi câu lệnh: private final ActivityResultLauncher<String> imagePicker =.
  private final ActivityResultLauncher<String> imagePicker =
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (uri != null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: selectedImageSource = uri.toString();.
          selectedImageSource = uri.toString();
          // Thực hiện lời gọi phương thức hoặc khởi tạo: edtImage.setText(selectedImageSource);.
          edtImage.setText(selectedImageSource);
          // Thực hiện lời gọi phương thức hoặc khởi tạo: loadPreview(selectedImageSource);.
          loadPreview(selectedImageSource);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
      });

  // Áp dụng annotation @Override cho phần tử bên dưới.
  @Override
  // Định nghĩa phương thức onCreate với phạm vi truy cập tương ứng.
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super.onCreate(savedInstanceState);.
    super.onCreate(savedInstanceState);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: setContentView(R.layout.activity_admin_product_form);.
    setContentView(R.layout.activity_admin_product_form);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: MaterialToolbar toolbar = findViewById(R.id.toolbarForm);.
    MaterialToolbar toolbar = findViewById(R.id.toolbarForm);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: toolbar.setNavigationOnClickListener(v -> finish());.
    toolbar.setNavigationOnClickListener(v -> finish());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName = findViewById(R.id.edtName);.
    edtName = findViewById(R.id.edtName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtDescription = findViewById(R.id.edtDescription);.
    edtDescription = findViewById(R.id.edtDescription);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPrice = findViewById(R.id.edtPrice);.
    edtPrice = findViewById(R.id.edtPrice);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtRating = findViewById(R.id.edtRating);.
    edtRating = findViewById(R.id.edtRating);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtStock = findViewById(R.id.edtStock);.
    edtStock = findViewById(R.id.edtStock);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtImage = findViewById(R.id.edtImageUrl);.
    edtImage = findViewById(R.id.edtImageUrl);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: spinnerCategory = findViewById(R.id.spinnerCategory);.
    spinnerCategory = findViewById(R.id.spinnerCategory);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: imgPreview = findViewById(R.id.imgPreview);.
    imgPreview = findViewById(R.id.imgPreview);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: MaterialButton btnPickImage = findViewById(R.id.btnPickImage);.
    MaterialButton btnPickImage = findViewById(R.id.btnPickImage);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSave = findViewById(R.id.btnSaveProduct);.
    btnSave = findViewById(R.id.btnSaveProduct);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());.
    categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);.
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: spinnerCategory.setAdapter(categoryAdapter);.
    spinnerCategory.setAdapter(categoryAdapter);

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnPickImage.setOnClickListener(v -> imagePicker.launch("image/*"));.
    btnPickImage.setOnClickListener(v -> imagePicker.launch("image/*"));

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSave.setOnClickListener(v -> saveProduct());.
    btnSave.setOnClickListener(v -> saveProduct());

    // Thực hiện lời gọi phương thức hoặc khởi tạo: editingProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);.
    editingProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: toolbar.setTitle(editingProductId > 0 ? "Edit product" : "Add product");.
    toolbar.setTitle(editingProductId > 0 ? "Edit product" : "Add product");

    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm = new ViewModelProvider(this).get(AdminProductFormVM.class);.
    vm = new ViewModelProvider(this).get(AdminProductFormVM.class);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.categories().observe(this, this::applyCategories);.
    vm.categories().observe(this, this::applyCategories);
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (editingProductId > 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: vm.product(editingProductId).observe(this, this::bindProduct);.
      vm.product(editingProductId).observe(this, this::bindProduct);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức applyCategories với phạm vi truy cập tương ứng.
  private void applyCategories(List<CategoryEntity> list){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryData.clear();.
    categoryData.clear();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (list != null) categoryData.addAll(list);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: List<String> names = new ArrayList<>();.
    List<String> names = new ArrayList<>();
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (CategoryEntity c : categoryData) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: names.add(c.name);.
      names.add(c.name);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryAdapter.clear();.
    categoryAdapter.clear();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryAdapter.addAll(names);.
    categoryAdapter.addAll(names);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: categoryAdapter.notifyDataSetChanged();.
    categoryAdapter.notifyDataSetChanged();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (pendingCategoryId > 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: selectCategory(pendingCategoryId);.
      selectCategory(pendingCategoryId);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức bindProduct với phạm vi truy cập tương ứng.
  private void bindProduct(ProductEntity product){
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (product == null) return;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName.setText(product.name);.
    edtName.setText(product.name);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtDescription.setText(product.description);.
    edtDescription.setText(product.description);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPrice.setText(String.format(Locale.getDefault(), "%.0f", product.price));.
    edtPrice.setText(String.format(Locale.getDefault(), "%.0f", product.price));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtRating.setText(product.rating == null ? "" : String.format(Locale.getDefault(), "%.1f", product.rating));.
    edtRating.setText(product.rating == null ? "" : String.format(Locale.getDefault(), "%.1f", product.rating));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtStock.setText(product.stock == null ? "" : String.valueOf(product.stock));.
    edtStock.setText(product.stock == null ? "" : String.valueOf(product.stock));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: edtImage.setText(product.imageUrl);.
    edtImage.setText(product.imageUrl);
    // Gán giá trị cho biến hoặc thuộc tính: selectedImageSource = product.imageUrl.
    selectedImageSource = product.imageUrl;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: loadPreview(selectedImageSource);.
    loadPreview(selectedImageSource);
    // Gán giá trị cho biến hoặc thuộc tính: pendingCategoryId = product.categoryId.
    pendingCategoryId = product.categoryId;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: selectCategory(product.categoryId);.
    selectCategory(product.categoryId);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức selectCategory với phạm vi truy cập tương ứng.
  private void selectCategory(int categoryId){
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (categoryData.isEmpty()) return;
    // Bắt đầu vòng lặp for để duyệt dữ liệu.
    for (int i = 0; i < categoryData.size(); i++) {
      // Kiểm tra điều kiện if để quyết định luồng xử lý.
      if (categoryData.get(i).categoryId == categoryId) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: spinnerCategory.setSelection(i);.
        spinnerCategory.setSelection(i);
        // Thoát khỏi vòng lặp hoặc switch hiện tại.
        break;
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức saveProduct với phạm vi truy cập tương ứng.
  private void saveProduct(){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String name = textOf(edtName);.
    String name = textOf(edtName);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: String desc = textOf(edtDescription);.
    String desc = textOf(edtDescription);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: double price = parseDouble(textOf(edtPrice));.
    double price = parseDouble(textOf(edtPrice));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: double rating = parseDouble(textOf(edtRating));.
    double rating = parseDouble(textOf(edtRating));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int stock = parseInt(textOf(edtStock));.
    int stock = parseInt(textOf(edtStock));
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int categoryId = getSelectedCategoryId();.
    int categoryId = getSelectedCategoryId();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: selectedImageSource = textOf(edtImage);.
    selectedImageSource = textOf(edtImage);

    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(name)) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: edtName.setError("Name cannot be empty");.
      edtName.setError("Name cannot be empty");
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (price <= 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: edtPrice.setError("Price must be greater than 0");.
      edtPrice.setError("Price must be greater than 0");
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (categoryId <= 0) {
      // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();.
      Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
      // Trả về kết quả ;.
      return;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (rating <= 0) rating = 4.5;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (rating > 5) rating = 5.0;
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (stock < 0) stock = 0;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: ProductEntity product = new ProductEntity();.
    ProductEntity product = new ProductEntity();
    // Gán giá trị cho biến hoặc thuộc tính: product.productId = editingProductId > 0 ? editingProductId : 0.
    product.productId = editingProductId > 0 ? editingProductId : 0;
    // Thực hiện lời gọi phương thức hoặc khởi tạo: product.name = name.trim();.
    product.name = name.trim();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: product.description = desc.trim();.
    product.description = desc.trim();
    // Gán giá trị cho biến hoặc thuộc tính: product.price = price.
    product.price = price;
    // Gán giá trị cho biến hoặc thuộc tính: product.rating = rating.
    product.rating = rating;
    // Gán giá trị cho biến hoặc thuộc tính: product.stock = stock.
    product.stock = stock;
    // Gán giá trị cho biến hoặc thuộc tính: product.categoryId = categoryId.
    product.categoryId = categoryId;
    // Gán giá trị cho biến hoặc thuộc tính: product.imageUrl = selectedImageSource.
    product.imageUrl = selectedImageSource;

    // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSave.setEnabled(false);.
    btnSave.setEnabled(false);
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    vm.save(product, new AdminProductFormVM.SaveCallback() {
      // Áp dụng annotation @Override và ghi đè phương thức onSuccess.
      @Override public void onSuccess(int productId) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(AdminProductFormActivity.this, "Product saved", Toast.LENGTH_SHORT).show();.
        Toast.makeText(AdminProductFormActivity.this, "Product saved", Toast.LENGTH_SHORT).show();
        // Thực hiện lời gọi phương thức hoặc khởi tạo: setResult(RESULT_OK);.
        setResult(RESULT_OK);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: finish();.
        finish();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }

      // Áp dụng annotation @Override và ghi đè phương thức onError.
      @Override public void onError(Throwable throwable) {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: btnSave.setEnabled(true);.
        btnSave.setEnabled(true);
        // Thực hiện lời gọi phương thức hoặc khởi tạo: Toast.makeText(AdminProductFormActivity.this, "Save failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();.
        Toast.makeText(AdminProductFormActivity.this, "Save failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức getSelectedCategoryId với phạm vi truy cập tương ứng.
  private int getSelectedCategoryId(){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: int position = spinnerCategory.getSelectedItemPosition();.
    int position = spinnerCategory.getSelectedItemPosition();
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (position < 0 || position >= categoryData.size()) return -1;
    // Trả về kết quả categoryData.get(position).categoryId;.
    return categoryData.get(position).categoryId;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức textOf với phạm vi truy cập tương ứng.
  private String textOf(TextInputEditText edt){
    // Trả về kết quả edt.getText() == null ? "" : edt.getText().toString().trim();.
    return edt.getText() == null ? "" : edt.getText().toString().trim();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức parseDouble với phạm vi truy cập tương ứng.
  private double parseDouble(String value){
    // Bắt đầu khối try để bắt lỗi có thể phát sinh.
    try { return Double.parseDouble(value); }
    // Bắt ngoại lệ phát sinh trong khối try phía trên.
    catch (NumberFormatException e){ return 0d; }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức parseInt với phạm vi truy cập tương ứng.
  private int parseInt(String value){
    // Bắt đầu khối try để bắt lỗi có thể phát sinh.
    try { return Integer.parseInt(value); }
    // Bắt ngoại lệ phát sinh trong khối try phía trên.
    catch (NumberFormatException e){ return 0; }
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức loadPreview với phạm vi truy cập tương ứng.
  private void loadPreview(String source){
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Object resolved = resolveImageSource(source);.
    Object resolved = resolveImageSource(source);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: Glide.with(this).
    Glide.with(this)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .load(resolved).
        .load(resolved)
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .apply(new RequestOptions().transform(new CenterCrop()).placeholder(R.drawable.bg_app_gradient).error(R.drawable.bg_app_gradient)).
        .apply(new RequestOptions().transform(new CenterCrop()).placeholder(R.drawable.bg_app_gradient).error(R.drawable.bg_app_gradient))
        // Thực hiện lời gọi phương thức hoặc khởi tạo: .into(imgPreview);.
        .into(imgPreview);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức resolveImageSource với phạm vi truy cập tương ứng.
  private Object resolveImageSource(String source){
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (TextUtils.isEmpty(source)) {
      // Trả về kết quả R.drawable.bg_app_gradient;.
      return R.drawable.bg_app_gradient;
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Kiểm tra điều kiện if để quyết định luồng xử lý.
    if (source.startsWith("content://") || source.startsWith("file://")) {
      // Trả về kết quả Uri.parse(source);.
      return Uri.parse(source);
    // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
    }
    // Trả về kết quả source;.
    return source;
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
