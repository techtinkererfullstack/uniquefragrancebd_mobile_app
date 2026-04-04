package com.example.uniquefragrancebd.presentation.checkout

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
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlaceOrder.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val phone = binding.etPhone.text.toString()
            viewModel.onPlaceOrder(address, phone)
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.apply {
                        checkoutProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                        tvSubtotal.text = getString(R.string.price_format, state.subtotal)
                        tvOrderTotal.text = getString(R.string.price_format, state.total)
                    }

                    if (state.orderPlaced) {
                        Toast.makeText(context, getString(R.string.order_placed_success), Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_checkoutFragment_to_productListFragment)
                    }

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