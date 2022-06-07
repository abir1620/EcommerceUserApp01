package com.example.ecommerceuserapp01.utils

const val collectionAdmin = "Admins"
const val collectionProduct = "Products"
const val collectionPurchase = "Purchases"
const val collectionOrder= "Orders"
const val collectionOrderDetails = "Order Details"
const val collectionUser = "Users"
const val collectionCart = "CartItems"
const val collectionCategory = "Categories"
const val collectionOrderSettings = "Order Settings"
const val documentOrderSettings = "Settings Document"

enum class CartAction {
    ADD, REMOVE
}


//Now the value will be inserted in the database so no enum
class PaymentMethod{
    companion object{
        const val cod = "Cash on Delivery"
        const val online = "Online Payment"
    }
}

class OrderStatus{
    companion object{
        const val pending = "Pending"
        const val delivered ="Delivered"
        const val cancelled = "Cancelled"
    }
}

//callback type
enum class DBResult{
    SUCCESS, FAILURE
}