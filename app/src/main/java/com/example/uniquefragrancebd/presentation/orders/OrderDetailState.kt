package com.example.uniquefragrancebd.presentation.orders

import com.example.uniquefragrancebd.domain.model.Order

data class OrderDetailState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)