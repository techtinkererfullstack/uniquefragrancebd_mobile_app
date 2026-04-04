package com.example.uniquefragrancebd.data.repository

import com.example.uniquefragrancebd.data.local.dao.OrderDao
import com.example.uniquefragrancebd.data.local.entity.OrderEntity
import com.example.uniquefragrancebd.data.local.entity.OrderItemEntity
import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.model.Order
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {

    override suspend fun placeOrder(userId: String, items: List<CartItem>, totalPrice: Double): String {
        val orderId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()

        val orderEntity = OrderEntity(
            orderId = orderId,
            userId = userId,
            totalPrice = totalPrice,
            timestamp = timestamp
        )

        val itemEntities = items.map { item ->
            OrderItemEntity(
                orderId = orderId,
                productId = item.product.id,
                name = item.product.name,
                price = item.product.price,
                quantity = item.quantity,
                imageUrl = item.product.imageUrl
            )
        }

        orderDao.insertOrder(orderEntity)
        orderDao.insertOrderItems(itemEntities)

        return orderId
    }

    override fun getOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { orderEntities ->
            orderEntities.map { entity ->
                val items = orderDao.getItemsForOrder(entity.orderId).map { itemEntity ->
                    CartItem(
                        product = Product(
                            id = itemEntity.productId,
                            name = itemEntity.name,
                            description = "",
                            price = itemEntity.price,
                            imageUrl = itemEntity.imageUrl,
                            category = ""
                        ),
                        quantity = itemEntity.quantity
                    )
                }
                Order(
                    id = entity.orderId,
                    items = items,
                    totalPrice = entity.totalPrice,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        val entity = orderDao.getOrderById(orderId) ?: return null
        val items = orderDao.getItemsForOrder(orderId).map { itemEntity ->
            CartItem(
                product = Product(
                    id = itemEntity.productId,
                    name = itemEntity.name,
                    description = "",
                    price = itemEntity.price,
                    imageUrl = itemEntity.imageUrl,
                    category = ""
                ),
                quantity = itemEntity.quantity
            )
        }
        return Order(
            id = entity.orderId,
            items = items,
            totalPrice = entity.totalPrice,
            timestamp = entity.timestamp
        )
    }
}