package com.example.uniquefragrancebd.domain.repository

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product)
    suspend fun removeFromCart(cartItem: CartItem)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun clearCart()
}