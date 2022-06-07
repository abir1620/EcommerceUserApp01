package com.example.ecommerceuserapp01.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ecommerceuserapp01.models.Order
import com.example.ecommerceuserapp01.models.OrderDetails
import com.example.ecommerceuserapp01.models.OrderSettings
import com.example.ecommerceuserapp01.repositories.OrderRepository
import com.example.ecommerceuserapp01.utils.DBResult

class OrderViewModel :  ViewModel(){
    private val orderRepository = OrderRepository()

    fun getOrderSettings() = orderRepository.getOrderSettings()

    fun getOrderByUser(userId: String) = orderRepository.getOrderByUser(userId)

    fun getDiscountAmount(total: Double, settings: OrderSettings) : Double{
        return (total * settings.discount) /100
    }

    fun getVatAmount(total: Double, settings: OrderSettings) : Double{
        val priceAfterDiscount = total - getDiscountAmount(total, settings)
        return (priceAfterDiscount * settings.vat) /100
    }

    fun getGrandTotal(total: Double, settings: OrderSettings): Double{
       return total - getDiscountAmount(total,settings) + getVatAmount(total, settings) + settings.deliveryCharge
    }

    //Now add another variable named callback, typed DBResult type and it will return unit
    fun addNewOrder(
        order: Order,
        orderDetailsList: MutableList<OrderDetails>,
        callback: (DBResult) ->Unit) {
        //Here call a method from repository
        orderRepository.addNewOrder(order, orderDetailsList, callback)
        //Now create this method in the repository

    }

}