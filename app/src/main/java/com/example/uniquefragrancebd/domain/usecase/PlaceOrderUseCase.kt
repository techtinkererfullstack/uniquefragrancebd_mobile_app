package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.repository.AuthRepository
import com.example.uniquefragrancebd.domain.repository.CartRepository
import com.example.uniquefragrancebd.domain.repository.OrderRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(items: List<CartItem>, totalPrice: Double): Result<String> {
        val user = authRepository.getCurrentUser() ?: return Result.failure(Exception("User not logged in"))
        
        return if (items.isEmpty()) {
            Result.failure(Exception("Cart is empty"))
        } else {
            try {
                val orderId = orderRepository.placeOrder(user.id, items, totalPrice)
                cartRepository.clearCart()
                Result.success(orderId)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}