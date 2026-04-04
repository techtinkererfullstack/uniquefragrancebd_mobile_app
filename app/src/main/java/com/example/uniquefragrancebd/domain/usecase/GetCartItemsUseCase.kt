package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return repository.getCartItems()
    }
}