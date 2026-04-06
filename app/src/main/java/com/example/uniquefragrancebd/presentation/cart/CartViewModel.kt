package com.example.uniquefragrancebd.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.model.CartItem
import com.example.uniquefragrancebd.domain.usecase.GetCartItemsUseCase
import com.example.uniquefragrancebd.domain.usecase.PlaceOrderUseCase
import com.example.uniquefragrancebd.domain.usecase.RemoveFromCartUseCase
import com.example.uniquefragrancebd.domain.usecase.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    init {
        observeCartItems()
    }

    private fun observeCartItems() {
        getCartItemsUseCase().onEach { items ->
            val total = items.sumOf { it.product.price * it.quantity }
            _state.update { it.copy(cartItems = items, totalPrice = total) }
        }.launchIn(viewModelScope)
    }

    fun onRemoveFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            removeFromCartUseCase(cartItem)
        }
    }

    fun onUpdateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, quantity)
        }
    }

    fun onCheckout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = placeOrderUseCase(_state.value.cartItems, _state.value.totalPrice)
            result.onSuccess { orderId ->
                _state.update { it.copy(isLoading = false, error = "Order placed: $orderId") }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}