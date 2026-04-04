package com.example.uniquefragrancebd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)