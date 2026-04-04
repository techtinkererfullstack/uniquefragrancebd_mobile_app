package com.example.uniquefragrancebd.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val userId: String,
    val totalPrice: Double,
    val timestamp: Long
)