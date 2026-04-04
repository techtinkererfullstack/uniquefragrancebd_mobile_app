package com.example.uniquefragrancebd.data.repository

import com.example.uniquefragrancebd.data.local.dao.WishlistDao
import com.example.uniquefragrancebd.data.local.entity.WishlistEntity
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getWishlistItems(): Flow<List<Product>> {
        return wishlistDao.getWishlistItems().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id,
                    name = entity.name,
                    description = "", // Not stored in wishlist for simplicity
                    price = entity.price,
                    imageUrl = entity.imageUrl,
                    category = ""
                )
            }
        }
    }

    override suspend fun addToWishlist(product: Product) {
        wishlistDao.addToWishlist(
            WishlistEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl
            )
        )
    }

    override suspend fun removeFromWishlist(product: Product) {
        wishlistDao.removeFromWishlist(
            WishlistEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl
            )
        )
    }

    override fun isInWishlist(productId: Int): Flow<Boolean> {
        return wishlistDao.isInWishlist(productId)
    }
}