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

public class AdminProductFormActivity extends AppCompatActivity {

  private static final String EXTRA_PRODUCT_ID = "extra_product_id";

  public static void start(Context context, int productId){
    Intent i = new Intent(context, AdminProductFormActivity.class);
    i.putExtra(EXTRA_PRODUCT_ID, productId);
    context.startActivity(i);
  }

  private AdminProductFormVM vm;
  private TextInputEditText edtName, edtDescription, edtPrice, edtRating, edtStock, edtImage;
  private Spinner spinnerCategory;
  private ImageView imgPreview;
  private MaterialButton btnSave;
  private final List<CategoryEntity> categoryData = new ArrayList<>();
  private ArrayAdapter<String> categoryAdapter;
  private int editingProductId = -1;
  private int pendingCategoryId = -1;
  private String selectedImageSource;

  private final ActivityResultLauncher<String> imagePicker =
      registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
          selectedImageSource = uri.toString();
          edtImage.setText(selectedImageSource);
          loadPreview(selectedImageSource);
        }
      });

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_product_form);

    MaterialToolbar toolbar = findViewById(R.id.toolbarForm);
    toolbar.setNavigationOnClickListener(v -> finish());

    edtName = findViewById(R.id.edtName);
    edtDescription = findViewById(R.id.edtDescription);
    edtPrice = findViewById(R.id.edtPrice);
    edtRating = findViewById(R.id.edtRating);
    edtStock = findViewById(R.id.edtStock);
    edtImage = findViewById(R.id.edtImageUrl);
    spinnerCategory = findViewById(R.id.spinnerCategory);
    imgPreview = findViewById(R.id.imgPreview);
    MaterialButton btnPickImage = findViewById(R.id.btnPickImage);
    btnSave = findViewById(R.id.btnSaveProduct);

    categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerCategory.setAdapter(categoryAdapter);

    btnPickImage.setOnClickListener(v -> imagePicker.launch("image/*"));

    btnSave.setOnClickListener(v -> saveProduct());

    editingProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
    toolbar.setTitle(editingProductId > 0 ? "Sua san pham" : "Them san pham");

    vm = new ViewModelProvider(this).get(AdminProductFormVM.class);
    vm.categories().observe(this, this::applyCategories);
    if (editingProductId > 0) {
      vm.product(editingProductId).observe(this, this::bindProduct);
    }
  }

  private void applyCategories(List<CategoryEntity> list){
    categoryData.clear();
    if (list != null) categoryData.addAll(list);
    List<String> names = new ArrayList<>();
    for (CategoryEntity c : categoryData) {
      names.add(c.name);
    }
    categoryAdapter.clear();
    categoryAdapter.addAll(names);
    categoryAdapter.notifyDataSetChanged();
    if (pendingCategoryId > 0) {
      selectCategory(pendingCategoryId);
    }
  }

  private void bindProduct(ProductEntity product){
    if (product == null) return;
    edtName.setText(product.name);
    edtDescription.setText(product.description);
    edtPrice.setText(String.format(Locale.getDefault(), "%.0f", product.price));
    edtRating.setText(product.rating == null ? "" : String.format(Locale.getDefault(), "%.1f", product.rating));
    edtStock.setText(product.stock == null ? "" : String.valueOf(product.stock));
    edtImage.setText(product.imageUrl);
    selectedImageSource = product.imageUrl;
    loadPreview(selectedImageSource);
    pendingCategoryId = product.categoryId;
    selectCategory(product.categoryId);
  }

  private void selectCategory(int categoryId){
    if (categoryData.isEmpty()) return;
    for (int i = 0; i < categoryData.size(); i++) {
      if (categoryData.get(i).categoryId == categoryId) {
        spinnerCategory.setSelection(i);
        break;
      }
    }
  }

  private void saveProduct(){
    String name = textOf(edtName);
    String desc = textOf(edtDescription);
    double price = parseDouble(textOf(edtPrice));
    double rating = parseDouble(textOf(edtRating));
    int stock = parseInt(textOf(edtStock));
    int categoryId = getSelectedCategoryId();
    selectedImageSource = textOf(edtImage);

    if (TextUtils.isEmpty(name)) {
      edtName.setError("Ten khong duoc de trong");
      return;
    }
    if (price <= 0) {
      edtPrice.setError("Gia phai lon hon 0");
      return;
    }
    if (categoryId <= 0) {
      Toast.makeText(this, "Vui long chon danh muc", Toast.LENGTH_SHORT).show();
      return;
    }
    if (rating <= 0) rating = 4.5;
    if (rating > 5) rating = 5.0;
    if (stock < 0) stock = 0;

    ProductEntity product = new ProductEntity();
    product.productId = editingProductId > 0 ? editingProductId : 0;
    product.name = name.trim();
    product.description = desc.trim();
    product.price = price;
    product.rating = rating;
    product.stock = stock;
    product.categoryId = categoryId;
    product.imageUrl = selectedImageSource;

    btnSave.setEnabled(false);
    vm.save(product, new AdminProductFormVM.SaveCallback() {
      @Override public void onSuccess(int productId) {
        Toast.makeText(AdminProductFormActivity.this, "Da luu san pham", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
      }

      @Override public void onError(Throwable throwable) {
        btnSave.setEnabled(true);
        Toast.makeText(AdminProductFormActivity.this, "Luu that bai: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private int getSelectedCategoryId(){
    int position = spinnerCategory.getSelectedItemPosition();
    if (position < 0 || position >= categoryData.size()) return -1;
    return categoryData.get(position).categoryId;
  }

  private String textOf(TextInputEditText edt){
    return edt.getText() == null ? "" : edt.getText().toString().trim();
  }

  private double parseDouble(String value){
    try { return Double.parseDouble(value); }
    catch (NumberFormatException e){ return 0d; }
  }

  private int parseInt(String value){
    try { return Integer.parseInt(value); }
    catch (NumberFormatException e){ return 0; }
  }

  private void loadPreview(String source){
    Object resolved = resolveImageSource(source);
    Glide.with(this)
        .load(resolved)
        .apply(new RequestOptions().transform(new CenterCrop()).placeholder(R.drawable.bg_app_gradient).error(R.drawable.bg_app_gradient))
        .into(imgPreview);
  }

  private Object resolveImageSource(String source){
    if (TextUtils.isEmpty(source)) {
      return R.drawable.bg_app_gradient;
    }
    if (source.startsWith("content://") || source.startsWith("file://")) {
      return Uri.parse(source);
    }
    return source;
  }
}
