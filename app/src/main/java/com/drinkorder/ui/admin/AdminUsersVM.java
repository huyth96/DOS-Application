// Khai báo package com.drinkorder.ui.admin cho toàn bộ lớp.
package com.drinkorder.ui.admin;

// Import android.app.Application để sử dụng các lớp hoặc hàm tương ứng.
import android.app.Application;
// Import android.os.Handler để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Handler;
// Import android.os.Looper để sử dụng các lớp hoặc hàm tương ứng.
import android.os.Looper;

// Import androidx.annotation.NonNull để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.NonNull;
// Import androidx.annotation.Nullable để sử dụng các lớp hoặc hàm tương ứng.
import androidx.annotation.Nullable;
// Import androidx.lifecycle.AndroidViewModel để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.AndroidViewModel;
// Import androidx.lifecycle.LiveData để sử dụng các lớp hoặc hàm tương ứng.
import androidx.lifecycle.LiveData;

// Import com.drinkorder.data.db.AppDatabase để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.AppDatabase;
// Import com.drinkorder.data.db.dao.UserDao để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.dao.UserDao;
// Import com.drinkorder.data.db.entity.UserEntity để sử dụng các lớp hoặc hàm tương ứng.
import com.drinkorder.data.db.entity.UserEntity;

// Import java.util.List để sử dụng các lớp hoặc hàm tương ứng.
import java.util.List;
// Import java.util.concurrent.Executor để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executor;
// Import java.util.concurrent.Executors để sử dụng các lớp hoặc hàm tương ứng.
import java.util.concurrent.Executors;

// Định nghĩa lớp AdminUsersVM kế thừa AndroidViewModel.
public class AdminUsersVM extends AndroidViewModel {

  // Định nghĩa interface ActionCallback.
  public interface ActionCallback {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onSuccess();.
    void onSuccess();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: void onError(Throwable throwable);.
    void onError(Throwable throwable);
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Khai báo thuộc tính với phạm vi truy cập: private final UserDao userDao.
  private final UserDao userDao;
  // Khai báo thuộc tính với phạm vi truy cập: private final Executor io = Executors.newSingleThreadExecutor().
  private final Executor io = Executors.newSingleThreadExecutor();
  // Khai báo thuộc tính với phạm vi truy cập: private final Handler main = new Handler(Looper.getMainLooper()).
  private final Handler main = new Handler(Looper.getMainLooper());
  // Khai báo thuộc tính với phạm vi truy cập: public final LiveData<List<UserEntity>> users.
  public final LiveData<List<UserEntity>> users;

  // Định nghĩa phương thức AdminUsersVM với phạm vi truy cập tương ứng.
  public AdminUsersVM(@NonNull Application application) {
    // Thực hiện lời gọi phương thức hoặc khởi tạo: super(application);.
    super(application);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: AppDatabase db = AppDatabase.get(application);.
    AppDatabase db = AppDatabase.get(application);
    // Thực hiện lời gọi phương thức hoặc khởi tạo: userDao = db.userDao();.
    userDao = db.userDao();
    // Thực hiện lời gọi phương thức hoặc khởi tạo: users = userDao.observeAll();.
    users = userDao.observeAll();
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }

  // Định nghĩa phương thức setBanStatus với phạm vi truy cập tương ứng.
  public void setBanStatus(@NonNull UserEntity user, boolean ban, @Nullable ActionCallback callback) {
    // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
    io.execute(() -> {
      // Bắt đầu khối try để bắt lỗi có thể phát sinh.
      try {
        // Thực hiện lời gọi phương thức hoặc khởi tạo: userDao.updateBanStatus(user.userId, ban);.
        userDao.updateBanStatus(user.userId, ban);
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: main.post(callback::onSuccess);.
          main.post(callback::onSuccess);
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Bắt đầu một khối lệnh mới liên quan đến cấu trúc ở trên.
      } catch (Throwable throwable) {
        // Kiểm tra điều kiện if để quyết định luồng xử lý.
        if (callback != null) {
          // Thực hiện lời gọi phương thức hoặc khởi tạo: main.post(() -> callback.onError(throwable));.
          main.post(() -> callback.onError(throwable));
        // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
        }
      // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
      }
    // Thực hiện lời gọi phương thức hoặc khởi tạo: });.
    });
  // Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
  }
// Đánh dấu ranh giới mở hoặc đóng của một khối lệnh.
}
