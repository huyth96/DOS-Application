
# DrinkOrder Android App

Ứng dụng đặt nước viết bằng **Java + MVVM + Room*
## 📦 Chức năng
- Đăng ký / Đăng nhập (demo: `user1/123456`)
- Xem danh mục & sản phẩm
- Xem chi tiết sản phẩm
- Giỏ hàng: thêm, xoá, tăng/giảm số lượng
- Thanh toán (giả lập, lưu vào Room DB)
- Lịch sử đơn hàng

## 🏗️ Công nghệ
- **UI**: Activity, Fragment, RecyclerView, Glide  
- **Data**: Room Database + seed từ `assets/seed.json`  
- **MVVM**: ViewModel + LiveData + Repository

## 🚀 Cài đặt
1. Clone repo:  
   git clone https://github.com/<your_username>/DrinkOrder.git
2. Mở bằng **Android Studio (Flamingo+)**
3. Run ▶️ (API 24+)

## 📂 Cấu trúc
app/java/com/drinkorder/
 ├─ data/ (db, dao, entity, repo)
 ├─ ui/   (login, home, cart, orders, detail)
 └─ vm/   (ViewModels)






