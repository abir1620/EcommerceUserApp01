package com.example.ecommerceuserapp01.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.adapters.ProductAdapter
import com.example.ecommerceuserapp01.databinding.FragmentProductListBinding
import com.example.ecommerceuserapp01.models.CartItem
import com.example.ecommerceuserapp01.models.Product
import com.example.ecommerceuserapp01.utils.CartAction
import com.example.ecommerceuserapp01.viewmodels.LoginViewModel
import com.example.ecommerceuserapp01.viewmodels.ProductViewModel
import com.example.ecommerceuserapp01.viewmodels.UserViewModel


class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val productViewModel : ProductViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProductListBinding.inflate(inflater, container, false)

        val adapter = ProductAdapter{action, cartItem ->
            performCartAction(action, cartItem)

        }
        val glm = GridLayoutManager(requireActivity(), 2)
        binding.productRV.layoutManager = glm
        binding.productRV.adapter = adapter

        //observe  authentication by the live data
        loginViewModel.authLD.observe(viewLifecycleOwner){
            if(it == LoginViewModel.Authentication.UNAUTHENTICATED){
                findNavController().navigate(R.id.action_productListFragment_to_userLoginFragment)
            }
        }

        userViewModel.getCurrentUserId()?.let{
            userViewModel.getAllCartItems(it)
                .observe(viewLifecycleOwner){cartList ->

                    //Available cart list we will take the product
                    //not before
                    //observe product list by the live data
                    productViewModel.getProducts().observe(viewLifecycleOwner){ productList->
                        //which product list we get we will submit it in the adapter
                        val tempProductList = mutableListOf<Product>()
                        for(product in productList){
                            for (item in cartList){
                                if(product.id == item.productId){
                                    product.inCart = true
                                }
                            }
                            tempProductList.add(product)
                        }
                        adapter.submitList(tempProductList)
                        //need to observe again

                    }
                }

        }



        return binding.root
    }

    private fun performCartAction(action: CartAction, cartItem: CartItem) {
       when(action){
           CartAction.ADD->{
               userViewModel.addToCart(loginViewModel.auth.currentUser?.uid!!, cartItem)
           }
           CartAction.REMOVE -> {
               userViewModel.removeFromCart(loginViewModel.auth.currentUser?.uid!!, cartItem)

           }
       }
    }


}