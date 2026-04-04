package com.example.uniquefragrancebd.presentation.detail

import com.example.uniquefragrancebd.domain.model.Product

data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)