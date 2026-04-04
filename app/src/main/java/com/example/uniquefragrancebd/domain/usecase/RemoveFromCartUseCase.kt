package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartItem: CartItem) {
        repository.removeFromCart(cartItem)
    }
}