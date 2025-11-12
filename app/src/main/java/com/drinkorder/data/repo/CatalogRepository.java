package com.drinkorder.data.repo;
import androidx.lifecycle.LiveData;
import com.drinkorder.data.db.dao.CategoryDao;
import com.drinkorder.data.db.dao.ProductDao;
import com.drinkorder.data.db.entity.CategoryEntity;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;

/**
 * Lớp CatalogRepository quản lý dữ liệu danh mục và sản phẩm thông qua các DAO.
 * Đây là lớp repository trong kiến trúc MVVM, làm trung gian giữa ViewModel và cơ sở dữ liệu.
 * Sử dụng dependency injection để nhận CategoryDao và ProductDao.
 */
public class CatalogRepository {
  /**
   * CategoryDao để truy cập dữ liệu danh mục.
   */
  private final CategoryDao c;

  /**
   * ProductDao để truy cập dữ liệu sản phẩm.
   */
  private final ProductDao p;

  /**
   * Constructor để khởi tạo repository với các DAO.
   * Sử dụng dependency injection để注入 các DAO.
   *
   * @param c CategoryDao để quản lý danh mục.
   * @param p ProductDao để quản lý sản phẩm.
   */
  public CatalogRepository(CategoryDao c, ProductDao p) {
    this.c = c;
    this.p = p;
  }

  /**
   * Lấy danh sách tất cả các danh mục dưới dạng LiveData.
   * Phương thức này gọi all() từ CategoryDao.
   *
   * @return LiveData chứa danh sách CategoryEntity.
   */
  public LiveData<java.util.List<CategoryEntity>> categories() {
    return c.all();
  }

  /**
   * Lấy danh sách sản phẩm theo danh mục cụ thể dưới dạng LiveData.
   * Phương thức này gọi byCategory() từ ProductDao.
   *
   * @param cid ID của danh mục cần lấy sản phẩm.
   * @return LiveData chứa danh sách ProductEntity thuộc danh mục đó.
   */
  public LiveData<java.util.List<ProductEntity>> productsByCategory(int cid) {
    return p.byCategory(cid);
  }

  /**
   * Lấy danh sách tất cả sản phẩm dưới dạng LiveData.
   * Phương thức này gọi all() từ ProductDao.
   *
   * @return LiveData chứa danh sách tất cả ProductEntity.
   */
  public LiveData<List<ProductEntity>> allProducts() {
    return p.all(); // lấy tất cả sản phẩm
  }
}
