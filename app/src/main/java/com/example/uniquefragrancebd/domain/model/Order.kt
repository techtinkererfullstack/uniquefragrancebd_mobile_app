package com.example.uniquefragrancebd.domain.model

data class Order(
    val id: String,
    val items: List<CartItem>,
    val totalPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)