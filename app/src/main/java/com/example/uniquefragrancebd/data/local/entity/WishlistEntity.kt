package com.example.uniquefragrancebd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uniquefragrancebd.domain.model.Product

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String
)

fun WishlistEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = "",
        price = price,
        imageUrl = imageUrl,
        category = ""
    )
}

fun Product.toWishlistEntity(): WishlistEntity {
    return WishlistEntity(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}