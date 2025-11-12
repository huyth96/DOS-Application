package com.drinkorder.data.db.dao;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.drinkorder.data.db.entity.ProductEntity;
import java.util.List;
/**
 * DAO cho bảng products trong Room, hỗ trợ CRUD trên ProductEntity.
 */
@Dao
public interface ProductDao {

  /**
   * Lấy sản phẩm theo danh mục, sắp xếp theo tên, dưới dạng LiveData.
   *
   * @param cid ID danh mục.
   * @return LiveData danh sách ProductEntity.
   */
  @Query("SELECT * FROM products WHERE categoryId=:cid ORDER BY name")
  LiveData<List<ProductEntity>> byCategory(int cid);

  /**
   * Lấy sản phẩm theo ID, dưới dạng LiveData.
   *
   * @param pid ID sản phẩm.
   * @return LiveData ProductEntity.
   */
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  LiveData<ProductEntity> byId(int pid);

  /**
   * Lấy sản phẩm theo ID, đồng bộ trực tiếp.
   *
   * @param pid ID sản phẩm.
   * @return ProductEntity.
   */
  @Query("SELECT * FROM products WHERE productId=:pid LIMIT 1")
  ProductEntity byIdNow(int pid);

  /**
   * Upsert danh sách sản phẩm (thay thế nếu xung đột).
   *
   * @param list Danh sách ProductEntity.
   */
  @Insert(onConflict=OnConflictStrategy.REPLACE)
  void upsertAll(List<ProductEntity> list);

  /**
   * Lấy tất cả sản phẩm sắp xếp theo tên, dưới dạng LiveData.
   *
   * @return LiveData danh sách ProductEntity.
   */
  @Query("SELECT * FROM products ORDER BY name")
  LiveData<List<ProductEntity>> all();

  /**
   * Lấy tất cả sản phẩm sắp xếp theo tên, đồng bộ trực tiếp.
   *
   * @return Danh sách ProductEntity.
   */
  @Query("SELECT * FROM products ORDER BY name")
  List<ProductEntity> allNow();

  /**
   * Chèn sản phẩm mới, trả về ID.
   *
   * @param product ProductEntity cần chèn.
   * @return ID sản phẩm.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(ProductEntity product);

  /**
   * Cập nhật sản phẩm, trả về số hàng ảnh hưởng.
   *
   * @param product ProductEntity cần cập nhật.
   * @return Số hàng cập nhật.
   */
  @Update
  int update(ProductEntity product);

  /**
   * Xóa sản phẩm, trả về số hàng ảnh hưởng.
   *
   * @param product ProductEntity cần xóa.
   * @return Số hàng xóa.
   */
  @Delete
  int delete(ProductEntity product);

  /**
   * Xóa sản phẩm theo ID.
   *
   * @param productId ID cần xóa.
   */
  @Query("DELETE FROM products WHERE productId=:productId")
  void deleteById(int productId);
}
