package com.example.uniquefragrancebd.domain.repository

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(userId: String, items: List<CartItem>, totalPrice: Double): String
    fun getOrders(): Flow<List<Order>>
    suspend fun getOrderById(orderId: String): Order?
}