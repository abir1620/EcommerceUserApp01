package com.example.ecommerceuserapp01.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecommerceuserapp01.models.CartItem
import com.example.ecommerceuserapp01.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {

    val userRepository = UserRepository()

    fun getCurrentUserId() = FirebaseAuth.getInstance().currentUser?.uid
    fun addToCart(userId: String, cartItem: CartItem) = userRepository.addToCart(userId, cartItem)
    fun removeFromCart(userId: String, cartItem: CartItem) = userRepository.removeFromCart(userId, cartItem)
     fun getAllCartItems(userId: String) = userRepository.getAllCartItems(userId)
     fun updateCartQuantity(userId: String, cartItem: CartItem) = userRepository.updateCartQuantity(userId, cartItem)
     fun getTotalCartItemPrice(cartList: List<CartItem>) : Double{
         var total = 0.0
         cartList.forEach {
             total += it.quantity * it.price!!
         }
         return total
     }

    fun getUser(userId: String) = userRepository.getUser(userId)
    //Now this method will call from the first of the CheckOutFragment

    fun updateUserAddress(userId: String, address: String) =userRepository.updateUserAddress(userId, address)
   //This method will be called under next button onClick Listener from CheckoutFragment

    fun clearCart(cartList: List<CartItem>) = userRepository.clearCart(getCurrentUserId()!!, cartList)
    //This method will be called when my order will be placed
    //so go to OrderConfirmationFragment
}