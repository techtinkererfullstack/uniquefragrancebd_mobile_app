package com.example.uniquefragrancebd.data.remote.dto

import com.example.uniquefragrancebd.data.local.entity.ProductEntity
import com.example.uniquefragrancebd.domain.model.Product

data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val images: List<String>,
    val category: String
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = images.firstOrNull() ?: "",
        category = category
    )
}

fun ProductDto.toEntity(): ProductEntity {
    val domain = this.toDomain()
    return ProductEntity(
        id = domain.id,
        name = domain.name,
        description = domain.description,
        price = domain.price,
        imageUrl = domain.imageUrl,
        category = domain.category,
        brand = domain.brand
    )
}