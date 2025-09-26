#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
fix_order_ui_to_entity_v2.py
A safer, Py3.8-compatible version of the fixer with simpler typing and quoting.
Run from your ANDROID PROJECT ROOT.
"""
import os, re, sys, shutil
from pathlib import Path

PKG = "com.drinkorder"
JAVA_BASE = Path("app/src/main/java") / PKG.replace('.', '/')
ENTITY_DIR = JAVA_BASE / "data/db/entity"
DAO_DIR    = JAVA_BASE / "data/db/dao"
UI_DIR     = JAVA_BASE / "ui/order"

def backup_once(path):
    bak = path.with_suffix(path.suffix + ".bak")
    if path.exists() and not bak.exists():
        shutil.copy2(path, bak)
        print("* Backup:", bak)

def read_text(path):
    return path.read_text(encoding="utf-8") if path and path.exists() else ""

def guess_fields(src, candidates):
    for name in candidates:
        pat = r"\b( public | private | protected )\s+[\w\<\>\[\]]+\s+" + re.escape(name) + r"\b"
        if re.search(pat, src):
            return name
    return None

def find_class_file(simple):
    p = ENTITY_DIR / (simple + ".java")
    if p.exists():
        return p
    for q in JAVA_BASE.rglob(simple + ".java"):
        # prefer entity path if found multiple
        return q
    return None

def detect_appdb_method():
    # find AppDatabase.java anywhere
    appdb = None
    for q in JAVA_BASE.rglob("AppDatabase.java"):
        appdb = q
        break
    if not appdb:
        return "getInstance"
    src = read_text(appdb)
    m = re.search(r"public\s+static\s+AppDatabase\s+(\w+)\s*\([^)]*(Context|Application)[^)]*\)", src)
    if m:
        return m.group(1)
    for name in ["getInstance", "getDatabase", "getDb", "instance", "INSTANCE"]:
        if name in src:
            return name
    return "getInstance"

def add_get_all_orders_if_missing():
    dao = DAO_DIR / "OrderDao.java"
    if not dao.exists():
        print("! OrderDao.java not found; skip getAllOrders() check.")
        return
    src = read_text(dao)
    if "getAllOrders(" in src:
        return
    backup_once(dao)
    addition = (
        "\n    @androidx.room.Query(\"SELECT * FROM orders ORDER BY createdAt DESC\")\n"
        "    androidx.lifecycle.LiveData<java.util.List<com.drinkorder.data.db.entity.OrderEntity>> getAllOrders();\n"
    )
    new_src = re.sub(r"}\s*\Z", addition + "}\n", src, flags=re.S)
    if new_src != src:
        dao.write_text(new_src, encoding="utf-8")
        print("~ Patched:", dao, "(added getAllOrders())")
    else:
        print("= Could not patch getAllOrders(); please add manually.")

def patch_file(path, replacers):
    if not path.exists():
        return
    src = read_text(path)
    new = src
    for a, b in replacers:
        new = re.sub(a, b, new)
    if new != src:
        backup_once(path)
        path.write_text(new, encoding="utf-8")
        print("~ Patched:", path)

def main():
    order_src   = read_text(find_class_file("OrderEntity"))
    item_src    = read_text(find_class_file("OrderItemEntity"))
    product_src = read_text(find_class_file("ProductEntity"))

    status_f  = guess_fields(order_src,  ["status","orderStatus","state","orderState"]) or "status"
    total_f   = guess_fields(order_src,  ["totalAmount","total","grandTotal","amount"]) or "totalAmount"
    created_f = guess_fields(order_src,  ["createdAt","createdTime","createdOn","timestamp","createdAtMillis"]) or "createdAt"

    qty_f   = guess_fields(item_src,     ["quantity","qty","count"]) or "quantity"
    unit_f  = guess_fields(item_src,     ["unitPrice","price","unit_cost","unitCost"]) or "unitPrice"
    name_f  = guess_fields(product_src,  ["name","productName","title","displayName"]) or "name"

    created_is_long_primitive = False
    if order_src:
        if re.search(r"\blong\s+" + re.escape(created_f) + r"\b", order_src):
            created_is_long_primitive = True

    appdb_method = detect_appdb_method()
    print("Detected -> status:{}, total:{}, createdAt:{} (long_primitive={}), qty:{}, unit:{}, productName:{}".format(
        status_f, total_f, created_f, created_is_long_primitive, qty_f, unit_f, name_f
    ))
    print("AppDatabase method -> {}(..)".format(appdb_method))

    # VM / OrdersFragment: getInstance -> detected name
    vm = UI_DIR / "OrderDetailVM.java"
    orders_frag = UI_DIR / "OrdersFragment.java"
    patch_file(vm, [(r"AppDatabase\.getInstance\(", "AppDatabase.{}(".format(appdb_method))])
    patch_file(orders_frag, [(r"AppDatabase\.getInstance\(", "AppDatabase.{}(".format(appdb_method))])

    # OrdersAdapter fields & types
    orders_ad = UI_DIR / "OrdersAdapter.java"
    rep = []
    rep.append((r"o\.status", "o.{}".format(status_f)))
    rep.append((r"o\.totalAmount", "o.{}".format(total_f)))
    rep.append((r"Float\.compare\(a\.totalAmount, b\.totalAmount\)", "Double.compare(a.{}, b.{})".format(total_f, total_f)))
    # createdAt equals -> primitive compare
    rep.append((r"o\.createdAt\.equals\(b\.createdAt\)", "o.{} == b.{}".format(created_f, created_f)))
    patch_file(orders_ad, rep)

    # OrderDetailFragment: fields
    frag = UI_DIR / "OrderDetailFragment.java"
    rep = []
    rep.append((r"data\.order\.status", "data.order.{}".format(status_f)))
    rep.append((r"data\.order\.totalAmount", "data.order.{}".format(total_f)))
    patch_file(frag, rep)

    # OrderLineAdapter: names & double
    line = UI_DIR / "OrderLineAdapter.java"
    rep = []
    rep.append((r"row\.product != null \? row\.product\.name", "row.product != null ? row.product.{}".format(name_f)))
    rep.append((r"int qty = row\.item != null \? row\.item\.quantity : 0;", "int qty = row.item != null ? row.item.{} : 0;".format(qty_f)))
    rep.append((r"float price = row\.item != null \? row\.item\.unitPrice : 0f;", "double price = row.item != null ? row.item.{} : 0d;".format(unit_f)))
    rep.append((r"float total = qty \* price;", "double total = qty * price;"))
    rep.append((r"String\.format\(\"%\.0f\", price\)", "String.format(\"%.0f\", price)"))
    rep.append((r"String\.format\(\"%\.0f\", total\)", "String.format(\"%.0f\", total)"))
    patch_file(line, rep)

    add_get_all_orders_if_missing()

    print("\nDone. Rebuild the project.")

if __name__ == "__main__":
    main()
