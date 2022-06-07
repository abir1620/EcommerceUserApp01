package com.example.ecommerceuserapp01.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.databinding.CartItemSampleRowBinding
import com.example.ecommerceuserapp01.databinding.FragmentOrderConfirmationBinding
import com.example.ecommerceuserapp01.models.CartItem
import com.example.ecommerceuserapp01.models.Order
import com.example.ecommerceuserapp01.models.OrderDetails
import com.example.ecommerceuserapp01.models.OrderSettings
import com.example.ecommerceuserapp01.utils.DBResult
import com.example.ecommerceuserapp01.utils.OrderStatus
import com.example.ecommerceuserapp01.viewmodels.OrderViewModel
import com.example.ecommerceuserapp01.viewmodels.UserViewModel
import com.google.firebase.Timestamp


class OrderConfirmationFragment : Fragment() {

  private lateinit var binding: FragmentOrderConfirmationBinding
  private val orderViewModel: OrderViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var orderSettings: OrderSettings? = null
    private var cartList: List<CartItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)

        arguments?.let {
            binding.deliveryAddressTV.text = it.getString("address")
            binding.paymentMethodTV.text = it.getString("payment")
        }
        orderViewModel.getOrderSettings().observe(viewLifecycleOwner) {settings->
            orderSettings= settings
            binding.discountLabelTV.text = "Discount(${settings.discount}%)"
            binding.vatLabelTV.text = "VAT(${settings.vat}%)"
            binding.deliveryChargeTV.text = "BDT${settings.deliveryCharge}"
            userViewModel.getAllCartItems(userViewModel.getCurrentUserId()!!)
                .observe(viewLifecycleOwner) {cartList ->
                    this.cartList = cartList
                    binding.totalTV.text = "BDT${userViewModel.getTotalCartItemPrice(cartList)}"
                    populateCartItemLayout(cartList, inflater)
                    binding.discountAmountTV.text =
                        "BDT${orderViewModel.getDiscountAmount(userViewModel.getTotalCartItemPrice(cartList), settings)}"

                    binding.vatAmountTV.text =
                        "BDT${orderViewModel.getVatAmount(userViewModel.getTotalCartItemPrice(cartList), settings)}"

                    binding.grandTotalTV.text =
                        "BDT${orderViewModel.getGrandTotal(
                            
                           total = userViewModel.getTotalCartItemPrice(cartList),
                           settings = settings
                        )}"
                }
        }

        binding.orderBtn.setOnClickListener {
            //After clicking the order button we have to create an object of order
            val order = Order(
                userId = userViewModel.getCurrentUserId(),
                orderDate = Timestamp.now(),
                deliveryCharge = orderSettings!!.deliveryCharge,
                vat = orderSettings!!.vat,
                discount = orderSettings!!.discount,
                paymentMethod = binding.paymentMethodTV.text.toString(),
                deliveryAddress = binding.deliveryAddressTV.text.toString(),
                orderStatus = OrderStatus.pending,
                grandTotal = orderViewModel.getGrandTotal(userViewModel.getTotalCartItemPrice(cartList), orderSettings!!)
            )

           //Now order details
            //Under this order which product have
            //that have in the cartList
            //So take a loop
           val orderDetailsList = mutableListOf<OrderDetails>()
            cartList.forEach {
                //Now starting a loop we will create an object of order details
                //And that will be a collection
                orderDetailsList.add(
                    OrderDetails(
                    productId = it.productId,
                    productName = it.productName,
                    productQuantity = it.quantity,
                    productPrice = it.price!!
                )
                )

            }

            //Now go to OrderViewModel
            //Now create a method in OrderViewModel
            //and add in the OrderRepository
            orderViewModel.addNewOrder(order, orderDetailsList){
                //If data will add in the data base we will send the SUCCESS
                //If not then FAILURE
                if(it ==DBResult.SUCCESS){
                   userViewModel.clearCart(cartList)
                    findNavController().navigate(R.id.action_orderConfirmationFragment_to_orderSuccessfulMessageFragment)
                } else{


                }
            }



        }


        return binding.root
        }

        private fun populateCartItemLayout(it: List<CartItem>?, inflater: LayoutInflater) {

            binding.cartItemsLinearLayout.removeAllViews()
            it?.let { cartList ->
                cartList.forEach {
                    val cartRowBinding = CartItemSampleRowBinding.inflate(inflater)
                    cartRowBinding.item = it
                    binding.cartItemsLinearLayout.addView(cartRowBinding.root)
                }
            }

        }
    }

