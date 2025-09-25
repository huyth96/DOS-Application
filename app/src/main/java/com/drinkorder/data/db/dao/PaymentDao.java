package com.drinkorder.data.db.dao;
import androidx.room.*;
import com.drinkorder.data.db.entity.PaymentEntity;
@Dao public interface PaymentDao { @Insert void insert(PaymentEntity p); }
