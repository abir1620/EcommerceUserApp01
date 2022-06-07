package com.example.ecommerceuserapp01.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.databinding.ProductItemBinding
import com.example.ecommerceuserapp01.models.CartItem
import com.example.ecommerceuserapp01.models.Product
import com.example.ecommerceuserapp01.utils.CartAction

class ProductAdapter(val cartAction: (CartAction, CartItem) -> Unit) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffUtil()){

    class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.product = product
        }
    }

    class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
      val binding =ProductItemBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
      )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        if(product.inCart){
            holder.binding.productRowCartBtn.setImageResource(R.drawable.ic_baseline_remove_shopping_cart_24)
        }else{
            holder.binding.productRowCartBtn.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24)

        }
        holder.itemView.setOnClickListener {
           //After clicking it will go for details page
        }

        holder.binding.productRowCartBtn.setOnClickListener {
            val cartItem = CartItem(
                productId = product.id,
                productName = product.name,
                price = product.salePrice
            )

            val action = if(product.inCart) CartAction.REMOVE else CartAction.ADD
            cartAction(action, cartItem)
            //we will pass it through callback to the product list fragment


        }
    }


}