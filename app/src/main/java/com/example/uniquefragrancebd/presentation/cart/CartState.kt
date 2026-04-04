package com.example.uniquefragrancebd.presentation.cart

import com.example.uniquefragrancebd.domain.model.CartItem

data class CartState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)