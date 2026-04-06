package com.example.uniquefragrancebd.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val brand: String = "Unique Fragrance BD"
)