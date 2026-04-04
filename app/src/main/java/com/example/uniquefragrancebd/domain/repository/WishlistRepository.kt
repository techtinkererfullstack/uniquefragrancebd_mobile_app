package com.example.uniquefragrancebd.domain.repository

import com.example.uniquefragrancebd.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlistItems(): Flow<List<Product>>
    suspend fun addToWishlist(product: Product)
    suspend fun removeFromWishlist(product: Product)
    fun isInWishlist(productId: Int): Flow<Boolean>
}