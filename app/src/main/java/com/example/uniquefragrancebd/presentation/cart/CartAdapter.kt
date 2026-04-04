package com.example.uniquefragrancebd.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.ItemCartBinding
import com.example.uniquefragrancebd.domain.model.CartItem

class CartAdapter(
    private val onRemoveClick: (CartItem) -> Unit,
    private val onIncreaseClick: (CartItem) -> Unit,
    private val onDecreaseClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvCartProductName.text = cartItem.product.name
                tvCartProductPrice.text = root.context.getString(R.string.price_format, cartItem.product.price)
                tvQuantity.text = cartItem.quantity.toString()

                Glide.with(ivCartProduct.context)
                    .load(cartItem.product.imageUrl)
                    .into(ivCartProduct)

                btnRemoveCart.setOnClickListener { onRemoveClick(cartItem) }
                btnIncrease.setOnClickListener { onIncreaseClick(cartItem) }
                btnDecrease.setOnClickListener { onDecreaseClick(cartItem) }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}