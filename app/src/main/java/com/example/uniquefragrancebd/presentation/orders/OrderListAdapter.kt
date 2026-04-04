package com.example.uniquefragrancebd.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.ItemOrderBinding
import com.example.uniquefragrancebd.domain.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderListAdapter(
    private val onOrderClick: (Order) -> Unit
) : ListAdapter<Order, OrderListAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = root.context.getString(R.string.order_id_format, order.id.take(8))
                tvOrderDate.text = dateFormat.format(Date(order.timestamp))
                
                val itemsSummary = order.items.joinToString { "${it.product.name} (x${it.quantity})" }
                tvOrderItems.text = itemsSummary
                
                tvOrderTotalPrice.text = root.context.getString(R.string.price_format, order.totalPrice)
                
                root.setOnClickListener { onOrderClick(order) }
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}