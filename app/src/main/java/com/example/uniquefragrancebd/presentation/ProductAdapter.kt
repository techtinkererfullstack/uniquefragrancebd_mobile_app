package com.example.uniquefragrancebd.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.ItemProductBinding
import com.example.uniquefragrancebd.domain.model.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit,
    private val onWishlistClick: (Product) -> Unit
) : ListAdapter<ProductAdapter.ProductUiModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    data class ProductUiModel(
        val product: Product,
        val isWishlisted: Boolean = false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val uiModel = getItem(position)
        holder.bind(uiModel)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ProductUiModel) {
            val product = uiModel.product
            binding.apply {
                tvProductName.text = product.name
                tvProductPrice.text = root.context.getString(R.string.price_format, product.price)
                
                Glide.with(ivProduct.context)
                    .load(product.imageUrl)
                    .into(ivProduct)

                btnWishlist.setImageResource(
                    if (uiModel.isWishlisted) android.R.drawable.btn_star_big_on 
                    else android.R.drawable.btn_star_big_off
                )

                root.setOnClickListener {
                    onProductClick(product)
                }
                
                btnAddToCart.setOnClickListener {
                    onAddToCartClick(product)
                }

                btnWishlist.setOnClickListener {
                    onWishlistClick(product)
                }
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductUiModel>() {
        override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
            return oldItem == newItem
        }
    }
}