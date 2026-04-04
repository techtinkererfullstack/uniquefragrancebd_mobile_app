package com.example.uniquefragrancebd.presentation.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentWishlistBinding
import com.example.uniquefragrancebd.presentation.ProductAdapter
import com.example.uniquefragrancebd.presentation.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishlistViewModel by viewModels()
    private val productListViewModel: ProductListViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                findNavController().navigate(
                    R.id.productDetailFragment,
                    bundle
                )
            },
            onAddToCartClick = { product ->
                productListViewModel.onAddToCart(product)
                Toast.makeText(context, getString(R.string.added_to_cart, product.name), Toast.LENGTH_SHORT).show()
            },
            onWishlistClick = { product ->
                viewModel.onRemoveFromWishlist(product)
            }
        )
        binding.rvWishlist.apply {
            adapter = this@WishlistFragment.adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val uiModels = state.wishlistItems.map { 
                        ProductAdapter.ProductUiModel(product = it, isWishlisted = true) 
                    }
                    adapter.submitList(uiModels)
                    binding.wishlistProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    binding.tvEmptyWishlist.visibility = if (!state.isLoading && state.wishlistItems.isEmpty()) View.VISIBLE else View.GONE
                    
                    state.error?.let { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
