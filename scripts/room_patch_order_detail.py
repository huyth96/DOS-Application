#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
room_patch_order_detail.py
Create/patch Room POJO relations + DAO queries to support "Order Detail" screen.

Run from your ANDROID PROJECT ROOT (same folder as settings.gradle).
Safe to re-run: existing files are backed up once and preserved.

What it does:
- Create POJO relations:
    data/db/pojo/OrderItemWithProduct.java
    data/db/pojo/OrderWithItems.java
- Patch OrderDao.java with queries:
    LiveData<OrderWithItems> getOrderWithItems(int orderId);
    LiveData<java.util.List<OrderItemWithProduct>> getItemsWithProduct(int orderId);
"""
import os, sys, shutil, re
from pathlib import Path

PKG = "com.drinkorder"
JAVA_BASE = Path("app/src/main/java") / PKG.replace('.', '/')
DAO_DIR = JAVA_BASE / "data/db/dao"
POJO_DIR = JAVA_BASE / "data/db/pojo"

FILES_TO_WRITE = {
    (POJO_DIR / "OrderItemWithProduct.java"): """package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.drinkorder.data.db.entity.OrderItemEntity;
import com.drinkorder.data.db.entity.ProductEntity;

/** One order item + its product detail */
public class OrderItemWithProduct {
    @Embedded public OrderItemEntity item;

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    public ProductEntity product;
}
""",

    (POJO_DIR / "OrderWithItems.java"): """package com.drinkorder.data.db.pojo;
import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;
import com.drinkorder.data.db.entity.OrderEntity;
import com.drinkorder.data.db.entity.OrderItemEntity;

/**
 * Order + list of items; each item contains its Product via nested relation.
 * Room will resolve nested @Relation to OrderItemWithProduct automatically.
 */
public class OrderWithItems {
    @Embedded public OrderEntity order;

    @Relation(
        entity = OrderItemEntity.class,
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    public List<OrderItemWithProduct> items;
}
"""
}

DAO_ADD = """
  // ====== Auto-added for Order Detail ======
  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
  androidx.lifecycle.LiveData<com.drinkorder.data.db.pojo.OrderWithItems> getOrderWithItems(int orderId);

  @androidx.room.Transaction
  @androidx.room.Query("SELECT * FROM order_items WHERE orderId = :orderId")
  androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.pojo.OrderItemWithProduct>> getItemsWithProduct(int orderId);
  // =========================================
"""

def find_order_dao():
    # Try typical location
    candidate = DAO_DIR / "OrderDao.java"
    if candidate.exists():
        return candidate
    # Fallback: scan
    for path in (JAVA_BASE).rglob("OrderDao.java"):
        return path
    return None

def ensure_dirs():
    POJO_DIR.mkdir(parents=True, exist_ok=True)

def write_once(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        # leave as-is (idempotent)
        print(f"= SKIP (exists): {path}")
        return
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"+ Created: {path}")

def backup_once(path: Path):
    bak = path.with_suffix(path.suffix + ".bak")
    if not bak.exists():
        shutil.copy2(path, bak)
        print(f"* Backup: {bak}")

def patch_order_dao(dao_path: Path):
    src = open(dao_path, "r", encoding="utf-8").read()
    if "getOrderWithItems(" in src:
        print("= OrderDao already has getOrderWithItems(...). No changes.")
        return
    # crude insert before final closing brace
    m = re.search(r"\}\s*\Z", src, flags=re.S)
    if not m:
        print("! Could not find class closing brace in OrderDao.java")
        return
    patched = src[:m.start()] + DAO_ADD + src[m.start():]
    backup_once(dao_path)
    with open(dao_path, "w", encoding="utf-8") as f:
        f.write(patched)
    print(f"~ Patched: {dao_path} (added Order Detail queries)")

def main():
    # sanity checks
    root_ok = Path("settings.gradle").exists() or Path("settings.gradle.kts").exists()
    app_ok = Path("app/build.gradle").exists() or Path("app/build.gradle.kts").exists()
    if not (root_ok and app_ok):
        print("! Warning: Not sure you're at the Android project root (settings.gradle & app/build.gradle not found). Proceeding anyway...")
    ensure_dirs()
    # Write POJO files
    for path, content in FILES_TO_WRITE.items():
        write_once(path, content)
    # Patch DAO
    dao = find_order_dao()
    if not dao:
        print("! Could not locate OrderDao.java. Please move it to app/src/main/java/com/drinkorder/data/db/dao/ or adjust PKG in this script.")
        sys.exit(1)
    patch_order_dao(dao)
    print("\nDone. Next steps:")
    print("1) Sync Gradle, then build.")
    print("2) Use OrderDao.getOrderWithItems(orderId) in your ViewModel to drive the Order Detail UI.")

if __name__ == "__main__":
    main()
