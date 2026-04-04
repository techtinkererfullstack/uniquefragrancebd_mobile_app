package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.repository.ProductRepository
import javax.inject.Inject

class RefreshProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke() {
        repository.refreshProducts()
    }
}