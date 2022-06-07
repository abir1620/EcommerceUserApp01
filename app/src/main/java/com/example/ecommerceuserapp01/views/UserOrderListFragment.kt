package com.example.ecommerceuserapp01.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceuserapp01.adapters.OrderAdapter
import com.example.ecommerceuserapp01.databinding.FragmentOrderListBinding
import com.example.ecommerceuserapp01.viewmodels.OrderViewModel
import com.example.ecommerceuserapp01.viewmodels.UserViewModel

class UserOrderListFragment : Fragment() {

    private lateinit var binding: FragmentOrderListBinding
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderListBinding.inflate(inflater, container, false)
        val adapter = OrderAdapter{

        }
        binding.orderRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.orderRV.adapter = adapter
        orderViewModel.getOrderByUser(userViewModel.getCurrentUserId()!!)
            .observe(viewLifecycleOwner){
                adapter.submitList(it)
            }

        return binding.root
    }


}