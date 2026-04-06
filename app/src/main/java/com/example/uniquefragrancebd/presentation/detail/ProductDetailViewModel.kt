package com.example.uniquefragrancebd.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.usecase.AddToCartUseCase
import com.example.uniquefragrancebd.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("productId")?.let { productId ->
            getProduct(productId)
        }
    }

    private fun getProduct(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val product = getProductByIdUseCase(id)
            if (product != null) {
                _state.update { it.copy(product = product, isLoading = false) }
            } else {
                _state.update { it.copy(error = "Product not found", isLoading = false) }
            }
        }
    }

    fun onAddToCart() {
        viewModelScope.launch {
            state.value.product?.let { product ->
                addToCartUseCase(product)
            }
        }
    }
}