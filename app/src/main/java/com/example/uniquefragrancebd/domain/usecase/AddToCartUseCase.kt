package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.addToCart(product)
    }
}