package com.example.uniquefragrancebd.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.ItemCartBinding
import com.example.uniquefragrancebd.domain.model.CartItem

/**
 * Simplified adapter for displaying items within an order detail screen.
 * Reuses item_cart layout but hides quantity controls.
 */
class OrderItemAdapter : ListAdapter<CartItem, OrderItemAdapter.OrderItemViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class OrderItemViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvCartProductName.text = cartItem.product.name
                tvCartProductPrice.text = root.context.getString(R.string.price_format, cartItem.product.price)
                tvQuantity.text = "Qty: ${cartItem.quantity}"

                Glide.with(ivCartProduct.context)
                    .load(cartItem.product.imageUrl)
                    .into(ivCartProduct)

                // Hide controls for order detail view
                btnRemoveCart.visibility = android.view.View.GONE
                btnIncrease.visibility = android.view.View.GONE
                btnDecrease.visibility = android.view.View.GONE
            }
        }
    }

    class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}