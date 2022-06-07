package com.example.ecommerceuserapp01.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceuserapp01.databinding.CartItemRowBinding
import com.example.ecommerceuserapp01.models.CartItem

class CartItemAdapter(val cartActionCallback: (CartItem)->Unit): ListAdapter<CartItem, CartItemAdapter.CartViewHolder>(CartDiffUtil()) {

    class CartViewHolder(val binding: CartItemRowBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(cartItem: CartItem){
            binding.cartItem=cartItem

        }
    }
    class CartDiffUtil: DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean
        {
           return oldItem.productId==newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean
        {
          return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=CartItemRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
        updateQuantityAndPrice(cartItem, holder.binding)
        holder.binding.addItemIv.setOnClickListener {
            cartItem.quantity += 1
            updateQuantityAndPrice(cartItem, holder.binding)
            cartActionCallback(cartItem)
        }

        holder.binding.minusItemIv.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                updateQuantityAndPrice(cartItem, holder.binding)
                cartActionCallback(cartItem)
            }
        }

        holder.binding.removeCartItemIv.setOnClickListener {
           cartActionCallback(cartItem)
        }


    }




    private fun updateQuantityAndPrice(cartItem: CartItem, binding: CartItemRowBinding) {
        binding.price = cartItem.quantity * cartItem.price!!
        binding.quantity = cartItem.quantity
    }
}