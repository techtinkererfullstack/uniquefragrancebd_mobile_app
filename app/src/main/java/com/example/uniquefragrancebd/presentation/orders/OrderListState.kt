package com.example.uniquefragrancebd.presentation.orders

import com.example.uniquefragrancebd.domain.model.Order

data class OrderListState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)