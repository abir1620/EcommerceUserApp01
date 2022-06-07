package com.example.ecommerceuserapp01.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.databinding.FragmentCheckoutBinding
import com.example.ecommerceuserapp01.utils.PaymentMethod
import com.example.ecommerceuserapp01.viewmodels.UserViewModel

class CheckoutFragment : Fragment() {

   private lateinit var binding: FragmentCheckoutBinding
   private val userViewModel: UserViewModel by activityViewModels()
    private var paymentMethod = PaymentMethod.cod
    private var deliveryAddress: String?= null
    //Now I will create a method in UserRepository to pick up the user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.paymentRG.setOnCheckedChangeListener { group, checkedId ->
            var rb: RadioButton = container!!.findViewById(checkedId)
            paymentMethod = rb.text.toString()
        }

        //This method will be called from UserViewModel
         userViewModel.getUser(userViewModel.getCurrentUserId()!!)
             .observe(viewLifecycleOwner){
                 deliveryAddress = it.userAddress
                 //Now we will check this address is still null or not
                 deliveryAddress?.let { address ->
                     binding.deliveryAddressEt.setText(address)
                 }
             }
        binding.nextPaymentBtn.setOnClickListener {

            val address = binding.deliveryAddressEt.text.toString()
            //todo: validate the field

            userViewModel.updateUserAddress(userViewModel.getCurrentUserId()!!, address)
            findNavController()
                .navigate(R.id.action_checkoutFragment_to_orderConfirmationFragment,
                args= bundleOf("address" to address, "payment" to paymentMethod))

        }



        return binding.root
    }

}