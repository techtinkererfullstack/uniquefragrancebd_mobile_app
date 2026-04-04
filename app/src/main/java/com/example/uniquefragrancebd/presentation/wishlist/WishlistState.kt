package com.example.uniquefragrancebd.presentation.wishlist

import com.example.uniquefragrancebd.domain.model.Product

data class WishlistState(
    val wishlistItems: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)