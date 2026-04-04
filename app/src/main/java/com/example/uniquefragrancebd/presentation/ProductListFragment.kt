package com.example.uniquefragrancebd.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentProductListBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupMenu()
        setupRecyclerView()
        observeState()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView
                
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.onSearch(newText ?: "")
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_cart -> {
                        findNavController().navigate(R.id.action_productListFragment_to_cartFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                findNavController().navigate(
                    R.id.action_productListFragment_to_productDetailFragment,
                    bundle
                )
            },
            onAddToCartClick = { product ->
                viewModel.onAddToCart(product)
                Toast.makeText(context, getString(R.string.added_to_cart, product.name), Toast.LENGTH_SHORT).show()
            },
            onWishlistClick = { product ->
                viewModel.onToggleWishlist(product)
            }
        )
        binding.rvProducts.apply {
            adapter = this@ProductListFragment.adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                        binding.tvEmptyState.visibility = if (!state.isLoading && state.products.isEmpty()) View.VISIBLE else View.GONE
                        
                        updateCategories(state.categories, state.selectedCategory)
                        
                        state.error?.let { error ->
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                launch {
                    viewModel.productsUiModels.collect { uiModels ->
                        adapter.submitList(uiModels)
                    }
                }
            }
        }
    }

    private fun updateCategories(categories: List<String>, selectedCategory: String?) {
        binding.cgCategories.removeAllViews()
        
        // Add "All" chip
        val allChip = createChip("All", selectedCategory == null)
        allChip.setOnClickListener { viewModel.onCategorySelected(null) }
        binding.cgCategories.addView(allChip)
        
        categories.forEach { category ->
            val chip = createChip(category, category == selectedCategory)
            chip.setOnClickListener { viewModel.onCategorySelected(category) }
            binding.cgCategories.addView(chip)
        }
    }

    private fun createChip(label: String, isSelected: Boolean): Chip {
        return Chip(requireContext()).apply {
            text = label
            isCheckable = true
            isChecked = isSelected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
