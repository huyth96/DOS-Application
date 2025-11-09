package com.drinkorder.data.db.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.drinkorder.data.db.entity.CartItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;

/**
 * Represents one cart row along with the associated product.
 */
public class CartItemWithProduct {
  @Embedded public CartItemEntity item;

  @Relation(
      parentColumn = "productId",
      entityColumn = "productId"
  )
  public ProductEntity product;
}
