package com.example.ecommerceuserapp01.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.adapters.CartItemAdapter
import com.example.ecommerceuserapp01.databinding.FragmentCartListBinding
import com.example.ecommerceuserapp01.models.CartItem
import com.example.ecommerceuserapp01.utils.collectionCart
import com.example.ecommerceuserapp01.viewmodels.UserViewModel


class CartListFragment : Fragment() {

    private lateinit var binding: FragmentCartListBinding
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartListBinding.inflate(inflater, container, false)

        val adapter = CartItemAdapter{
         userViewModel.updateCartQuantity(userViewModel.getCurrentUserId()!!, it)
        }
        binding.cartItemRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.cartItemRV.adapter=adapter
        //binding.cartItemRV.adapter = adapter
        //At first take all card model from card collection
        //This card collection is found user document
        //so we need userId
        //we can get userId from userViewModel
        //So we can make a method in userViewModel that will return userId

        //Calling the getAllCartItems from userRepository through UserViewModel
        userViewModel.getAllCartItems(userViewModel.getCurrentUserId()!!)
            .observe(viewLifecycleOwner){
                //Now we need a CartItemAdapter
                //go to CartItemAdapter
                //When card Item will come we will submit it in the adapter
                adapter.submitList(it)
                binding.checkout.isEnabled= !it.isNullOrEmpty()
                binding.totalAmountTv.text ="BDT${userViewModel.getTotalCartItemPrice(it)}"

            }

        //remove from cart
       // userViewModel.removeFromCart(userViewModel.getCurrentUserId()!!, )

     binding.checkout.setOnClickListener {
         findNavController().navigate(R.id.action_cartListFragment_to_checkoutFragment)
     }

        return binding.root
    }

}