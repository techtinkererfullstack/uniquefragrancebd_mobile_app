package com.example.uniquefragrancebd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uniquefragrancebd.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val brand: String
)

fun ProductEntity.toDomain(): Product {
    return Product(id, name, description, price, imageUrl, category, brand)
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(id, name, description, price, imageUrl, category, brand)
}