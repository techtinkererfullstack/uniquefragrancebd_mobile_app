package com.example.uniquefragrancebd.presentation.orders

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
import com.example.uniquefragrancebd.databinding.FragmentOrderListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderListViewModel by viewModels()
    private lateinit var adapter: OrderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = OrderListAdapter { order ->
            val bundle = Bundle().apply {
                putString("orderId", order.id)
            }
            findNavController().navigate(
                R.id.action_orderListFragment_to_orderDetailFragment,
                bundle
            )
        }
        binding.rvOrders.apply {
            adapter = this@OrderListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.orders)
                    binding.orderProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    binding.tvEmptyOrders.visibility = if (!state.isLoading && state.orders.isEmpty()) View.VISIBLE else View.GONE
                    
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