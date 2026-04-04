package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(category: String): Flow<List<Product>> {
        return repository.getProductsByCategory(category)
    }
}