package com.example.uniquefragrancebd.presentation.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.usecase.GetOrderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(OrderDetailState())
    val state: StateFlow<OrderDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("orderId")?.let { orderId ->
            getOrder(orderId)
        }
    }

    private fun getOrder(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val order = getOrderByIdUseCase(id)
            if (order != null) {
                _state.update { it.copy(order = order, isLoading = false) }
            } else {
                _state.update { it.copy(error = "Order not found", isLoading = false) }
            }
        }
    }
}