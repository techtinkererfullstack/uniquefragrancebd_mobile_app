package com.example.uniquefragrancebd.data.repository

import com.example.uniquefragrancebd.data.local.dao.ProductDao
import com.example.uniquefragrancebd.data.local.entity.toDomain
import com.example.uniquefragrancebd.data.remote.ApiService
import com.example.uniquefragrancebd.data.remote.dto.toEntity
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCategories(): Flow<List<String>> {
        return productDao.getAllCategories()
    }

    override suspend fun getProductById(id: String): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun refreshProducts() {
        try {
            val remoteProducts = apiService.getProducts()
            productDao.deleteAllProducts()
            productDao.insertProducts(remoteProducts.map { it.toEntity() })
        } catch (e: Exception) {
            // Handle error (e.g., logging)
        }
    }
}