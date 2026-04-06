package com.example.uniquefragrancebd.data.repository

import com.example.uniquefragrancebd.data.local.dao.CartDao
import com.example.uniquefragrancebd.data.local.entity.CartEntity
import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    product = Product(
                        id = entity.productId,
                        name = entity.name,
                        description = "", // Not stored in cart entity for simplicity
                        price = entity.price,
                        imageUrl = entity.imageUrl,
                        category = ""
                    ),
                    quantity = entity.quantity
                )
            }
        }
    }

    override suspend fun addToCart(product: Product) {
        cartDao.addToCart(
            CartEntity(
                productId = product.id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                quantity = 1 // Initial quantity
            )
        )
    }

    override suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.removeFromCart(
            CartEntity(
                productId = cartItem.product.id,
                name = cartItem.product.name,
                price = cartItem.product.price,
                imageUrl = cartItem.product.imageUrl,
                quantity = cartItem.quantity
            )
        )
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        cartDao.updateQuantity(productId, quantity)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}