package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Order
import com.example.uniquefragrancebd.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): Order? {
        return repository.getOrderById(orderId)
    }
}