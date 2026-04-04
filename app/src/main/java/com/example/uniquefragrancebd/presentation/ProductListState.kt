package com.example.uniquefragrancebd.presentation

import com.example.uniquefragrancebd.domain.model.Product

data class ProductListState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)