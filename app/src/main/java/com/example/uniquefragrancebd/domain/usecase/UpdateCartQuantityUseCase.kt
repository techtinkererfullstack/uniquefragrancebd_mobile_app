package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productId: Int, quantity: Int) {
        if (quantity > 0) {
            repository.updateQuantity(productId, quantity)
        }
    }
}