package com.example.uniquefragrancebd.presentation.checkout

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.model.User

data class CheckoutState(
    val cartItems: List<CartItem> = emptyList(),
    val user: User? = null,
    val subtotal: Double = 0.0,
    val shippingFee: Double = 100.0, // Updated shipping fee to a more realistic BD value if needed
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderPlaced: Boolean = false
)