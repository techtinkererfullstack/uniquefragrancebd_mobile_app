package com.example.uniquefragrancebd.presentation.checkout

import com.example.uniquefragrancebd.domain.model.CartItem

data class CheckoutState(
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingFee: Double = 10.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderPlaced: Boolean = false
)