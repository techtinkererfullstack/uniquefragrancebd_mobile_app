package com.example.uniquefragrancebd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniquefragrancebd.domain.model.Product
import com.example.uniquefragrancebd.domain.usecase.*
import com.example.uniquefragrancebd.presentation.ProductAdapter.ProductUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    private val _productsUiModels = MutableStateFlow<List<ProductUiModel>>(emptyList())
    val productsUiModels: StateFlow<List<ProductUiModel>> = _productsUiModels.asStateFlow()

    private var searchJob: Job? = null
    private var productsJob: Job? = null

    init {
        loadCategories()
        observeProductsAndWishlist()
        refreshProducts()
    }

    private fun loadCategories() {
        getCategoriesUseCase().onEach { categories ->
            _state.update { it.copy(categories = categories) }
        }.launchIn(viewModelScope)
    }

    private fun observeProductsAndWishlist() {
        combine(
            getProductsUseCase(),
            getWishlistUseCase()
        ) { products, wishlist ->
            products.map { product ->
                ProductUiModel(
                    product = product,
                    isWishlisted = wishlist.any { it.id == product.id }
                )
            }
        }.onEach { uiModels ->
            _productsUiModels.value = uiModels
            _state.update { it.copy(products = uiModels.map { m -> m.product }) }
        }.launchIn(viewModelScope)
    }

    fun onCategorySelected(category: String?) {
        productsJob?.cancel()
        val flow = if (category == null) {
            getProductsUseCase()
        } else {
            getProductsByCategoryUseCase(category)
        }

        productsJob = combine(
            flow,
            getWishlistUseCase()
        ) { products, wishlist ->
            products.map { product ->
                ProductUiModel(
                    product = product,
                    isWishlisted = wishlist.any { it.id == product.id }
                )
            }
        }.onEach { uiModels ->
            _productsUiModels.value = uiModels
            _state.update { it.copy(selectedCategory = category) }
        }.launchIn(viewModelScope)
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                refreshProductsUseCase()
                _state.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun onSearch(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            onCategorySelected(_state.value.selectedCategory)
            return
        }
        searchJob = viewModelScope.launch {
            delay(500L)
            combine(
                searchProductsUseCase(query),
                getWishlistUseCase()
            ) { products, wishlist ->
                products.map { product ->
                    ProductUiModel(
                        product = product,
                        isWishlisted = wishlist.any { it.id == product.id }
                    )
                }
            }.onEach { uiModels ->
                _productsUiModels.value = uiModels
            }.launchIn(viewModelScope)
        }
    }

    fun onAddToCart(product: Product) {
        viewModelScope.launch {
            try {
                addToCartUseCase(product)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Could not add to cart") }
            }
        }
    }

    fun onToggleWishlist(product: Product) {
        viewModelScope.launch {
            val isWishlisted = _productsUiModels.value.find { it.product.id == product.id }?.isWishlisted ?: false
            if (isWishlisted) {
                removeFromWishlistUseCase(product)
            } else {
                addToWishlistUseCase(product)
            }
        }
    }
}