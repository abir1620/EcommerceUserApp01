package com.example.ecommerceuserapp01.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerceuserapp01.repositories.ProductRepository

class ProductViewModel: ViewModel() {
    val repository = ProductRepository()
    val statusLD = MutableLiveData<String>()


    fun getOrderSettings() = repository.getOrderSettings()
    fun getProducts() = repository.getAllProducts()
    fun getProductByProductId(id : String) = repository.getProductByProductId(id)
    fun getCategories() = repository.getAllCategories()


}