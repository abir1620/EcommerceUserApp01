package com.example.ecommerceuserapp01.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.databinding.FragmentLoginBinding
import com.example.ecommerceuserapp01.viewmodels.LoginViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        //observe by the live data
        loginViewModel.authLD.observe(viewLifecycleOwner){
            if(it == LoginViewModel.Authentication.AUTHENTICATED){
               // findNavController().popBackStack()
                findNavController().navigate(R.id.action_userLoginFragment_to_productListFragment)
            }
        }

        loginViewModel.errorMsgLD.observe(viewLifecycleOwner){
            binding.errorMsgTV.text = it
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passwordEt.text.toString()

            //todo: validates fields
            loginViewModel.loginUser(email, pass)
        }

        binding.createNewUserText.setOnClickListener {
           findNavController().navigate(R.id.action_userLoginFragment_to_userRegistrationFragment)
        }
        return binding.root
    }


}