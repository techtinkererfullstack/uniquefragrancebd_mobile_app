package com.example.uniquefragrancebd.data.remote.dto

import com.example.uniquefragrancebd.data.local.entity.ProductEntity
import com.example.uniquefragrancebd.domain.model.Product

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val brand: String
)

fun ProductDto.toDomain(): Product {
    return Product(id, name, description, price, imageUrl, category, brand)
}

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(id, name, description, price, imageUrl, category, brand)
}