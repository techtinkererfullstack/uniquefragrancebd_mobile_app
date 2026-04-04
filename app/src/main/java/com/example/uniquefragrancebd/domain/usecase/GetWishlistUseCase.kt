package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getWishlistItems()
    }
}