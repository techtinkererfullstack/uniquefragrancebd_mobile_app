package com.example.uniquefragrancebd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uniquefragrancebd.data.local.dao.CartDao
import com.example.uniquefragrancebd.data.local.dao.OrderDao
import com.example.uniquefragrancebd.data.local.dao.ProductDao
import com.example.uniquefragrancebd.data.local.dao.WishlistDao
import com.example.uniquefragrancebd.data.local.entity.CartEntity
import com.example.uniquefragrancebd.data.local.entity.OrderEntity
import com.example.uniquefragrancebd.data.local.entity.OrderItemEntity
import com.example.uniquefragrancebd.data.local.entity.ProductEntity
import com.example.uniquefragrancebd.data.local.entity.WishlistEntity

@Database(
    entities = [
        ProductEntity::class,
        CartEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        WishlistEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val cartDao: CartDao
    abstract val orderDao: OrderDao
    abstract val wishlistDao: WishlistDao
}