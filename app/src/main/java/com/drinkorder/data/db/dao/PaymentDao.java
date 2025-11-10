// Khai báo package com.drinkorder.data.db.dao cho toàn bộ lớp.
package com.drinkorder.data.db.dao;
// Import androidx.room.* để sử dụng các lớp hoặc hàm tương ứng.
import androidx.room.*;
// Import com.drinkorder.data.db.entity.PaymentEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.PaymentEntity;
// Định nghĩa interface PaymentDao.
@Dao public interface PaymentDao { @Insert void insert(PaymentEntity p); }
