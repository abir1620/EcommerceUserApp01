package com.example.ecommerceuserapp01.models

data class Product(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var category: String? = null,
    var imageUrl: String? = null,
    var salePrice: Double? = 0.0,
    var available: Boolean = true,
    var rating: Double = 0.0,
    var inCart: Boolean = false
)
