package com.example.uniquefragrancebd.presentation.detail

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
import com.bumptech.glide.Glide
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.btnDetailAddToCart.setOnClickListener {
            viewModel.onAddToCart()
            Toast.makeText(context, getString(R.string.added_to_cart, viewModel.state.value.product?.name ?: ""), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.detailProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    
                    state.product?.let { product ->
                        binding.apply {
                            tvDetailName.text = product.name
                            tvDetailBrand.text = product.brand
                            tvDetailPrice.text = getString(R.string.price_format, product.price)
                            tvDetailDescription.text = product.description
                            
                            Glide.with(ivProductDetail.context)
                                .load(product.imageUrl)
                                .into(ivProductDetail)
                        }
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