package com.example.uniquefragrancebd.domain.usecase

import com.example.uniquefragrancebd.domain.model.Order
import com.example.uniquefragrancebd.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        return repository.getOrders()
    }
}