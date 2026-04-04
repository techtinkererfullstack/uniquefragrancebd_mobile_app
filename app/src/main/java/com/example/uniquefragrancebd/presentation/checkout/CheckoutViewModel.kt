package com.example.uniquefragrancebd.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.model.User
import com.example.uniquefragrancebd.domain.repository.AuthRepository
import com.example.uniquefragrancebd.domain.usecase.GetCartItemsUseCase
import com.example.uniquefragrancebd.domain.usecase.PlaceOrderUseCase
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
class CheckoutViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    init {
        observeCartItems()
        loadUserDetails()
    }

    private fun observeCartItems() {
        getCartItemsUseCase().onEach { items ->
            val subtotal = items.sumOf { it.product.price * it.quantity }
            _state.update { 
                it.copy(
                    cartItems = items, 
                    subtotal = subtotal,
                    total = subtotal + it.shippingFee
                ) 
            }
        }.launchIn(viewModelScope)
    }

    private fun loadUserDetails() {
        authRepository.getCurrentUser()?.let { user ->
            _state.update { it.copy(user = user) }
        }
    }

    fun onPlaceOrder(address: String, phone: String) {
        if (address.isBlank() || phone.isBlank()) {
            _state.update { it.copy(error = "Please enter valid shipping details") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = placeOrderUseCase(_state.value.cartItems, _state.value.total)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, orderPlaced = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}