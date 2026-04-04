package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }
}