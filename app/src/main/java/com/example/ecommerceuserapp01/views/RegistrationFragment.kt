package com.example.ecommerceuserapp01.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.databinding.FragmentRegistrationBinding
import com.example.ecommerceuserapp01.viewmodels.LoginViewModel


class RegistrationFragment : Fragment() {

private lateinit var binding: FragmentRegistrationBinding
private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegistrationBinding.inflate(inflater, container, false)

        //observe by the live data
        loginViewModel.authLD.observe(viewLifecycleOwner){
            if(it==LoginViewModel.Authentication.AUTHENTICATED){
                //findNavController().popBackStack()
                findNavController().navigate(R.id.action_userRegistrationFragment_to_userLoginFragment)
            }
        }

        loginViewModel.errorMsgLD.observe(viewLifecycleOwner){
            binding.errorMsgTV.text = it
        }

        binding.registrationBtn.setOnClickListener {
            val name=binding.nameEt.text.toString()
            val mobile=binding.mobileEt.text.toString()
            val email=binding.emailEt.text.toString()
            val pass=binding.passwordEt.text.toString()
            val confirmPass=binding.confirmPasswordEt.text.toString()

            loginViewModel.registerUser(name, mobile, email, pass,confirmPass)
        }

        return binding.root

    }

}