package com.example.uniquefragrancebd.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.usecase.AddToWishlistUseCase
import com.example.uniquefragrancebd.domain.usecase.GetWishlistUseCase
import com.example.uniquefragrancebd.domain.usecase.RemoveFromWishlistUseCase
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
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WishlistState())
    val state: StateFlow<WishlistState> = _state.asStateFlow()

    init {
        observeWishlist()
    }

    private fun observeWishlist() {
        _state.update { it.copy(isLoading = true) }
        getWishlistUseCase().onEach { items ->
            _state.update { it.copy(wishlistItems = items, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun onRemoveFromWishlist(product: Product) {
        viewModelScope.launch {
            removeFromWishlistUseCase(product)
        }
    }

    fun onAddToWishlist(product: Product) {
        viewModelScope.launch {
            addToWishlistUseCase(product)
        }
    }
}