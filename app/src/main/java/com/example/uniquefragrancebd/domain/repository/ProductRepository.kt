package com.example.uniquefragrancebd.domain.repository

import com.example.uniquefragrancebd.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun getCategories(): Flow<List<String>>
    suspend fun getProductById(id: String): Product?
    suspend fun refreshProducts()
}