package com.example.uniquefragrancebd.presentation.cart

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentCartBinding
import com.example.uniquefragrancebd.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeState()
        
        binding.btnCheckout.setOnClickListener {
            if (authViewModel.getCurrentUser() != null) {
                if (viewModel.state.value.cartItems.isNotEmpty()) {
                    findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
                } else {
                    Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            onRemoveClick = { item -> viewModel.onRemoveFromCart(item) },
            onIncreaseClick = { item -> viewModel.onUpdateQuantity(item.product.id, item.quantity + 1) },
            onDecreaseClick = { item -> viewModel.onUpdateQuantity(item.product.id, item.quantity - 1) }
        )
        binding.rvCart.apply {
            adapter = this@CartFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.cartItems)
                    binding.tvTotalPrice.text = getString(R.string.price_format, state.totalPrice)
                    
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