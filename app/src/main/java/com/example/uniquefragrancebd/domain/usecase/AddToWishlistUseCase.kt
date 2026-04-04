package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.WishlistRepository
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.addToWishlist(product)
    }
}