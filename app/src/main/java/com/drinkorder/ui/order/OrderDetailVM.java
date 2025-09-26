package com.drinkorder.ui.order;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drinkorder.data.db.AppDatabase;
import com.drinkorder.data.db.dao.OrderDao;
import com.drinkorder.data.db.pojo.OrderWithItems;

public class OrderDetailVM extends AndroidViewModel {
    private final OrderDao orderDao;
    private final MutableLiveData<Integer> orderId = new MutableLiveData<>();
    public final LiveData<OrderWithItems> order;

    public OrderDetailVM(@NonNull Application app) {
        super(app);
        AppDatabase db = AppDatabase.get(app);
        orderDao = db.orderDao();

        order = Transformations.switchMap(orderId, id -> {
            if (id == null) return new MediatorLiveData<>();
            return orderDao.getOrderWithItems(id);
        });
    }

    public void setOrderId(int id) {
        Integer cur = orderId.getValue();
        if (cur == null || cur != id) orderId.setValue(id);
    }
}
