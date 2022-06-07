package com.example.ecommerceuserapp01.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecommerceuserapp01.models.Order
import com.example.ecommerceuserapp01.models.OrderDetails
import com.example.ecommerceuserapp01.models.OrderSettings
import com.example.ecommerceuserapp01.utils.*
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {
    private val db= FirebaseFirestore.getInstance()

    fun getOrderSettings() : LiveData<OrderSettings>{
        val settingsLD = MutableLiveData<OrderSettings>()
        db.collection(collectionOrderSettings)
            .document(documentOrderSettings)
            .addSnapshotListener { value, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                settingsLD.value = value!!.toObject(OrderSettings::class.java)
            }
        return settingsLD
    }

    fun getOrderByUser(userId: String) : LiveData<List<Order>>{
        val orderLD = MutableLiveData<List<Order>>()
        db.collection(collectionOrder)
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                val tempList = mutableListOf<Order>()
                for(doc in value!!.documents){
                   doc.toObject(Order::class.java)?.let{tempList.add(it)}
                }
                orderLD.value =tempList
            }
        return orderLD
    }

    fun addNewOrder(
        order: Order,
        orderDetailsList: MutableList<OrderDetails>,
        callback: (DBResult) -> Unit)
    {
        //this callback I have to take in the OrderConfirmationFragment
        //Now write on the database
        //we use batch because data will be inserted both collection
        //so create an instance of batch
        val wb = db.batch()
        //Now in which document this order will be inserted, create that empty document
        val orderDoc = db.collection(collectionOrder).document()
        //Now this blank document id will be saved in the orderId
        order.orderId = orderDoc.id
        //Now we can say the wb, you will set orderDoc to order
        wb.set(orderDoc, order)
        //Now start a loop in the above orderDetailsList
        orderDetailsList.forEach{
            //For per order details we will create one particular document that means how many items in the cart
            //for each we will create a particular document
            //And this document will remain which collection
            //under the orderdocument create a collection of orderdetails
            val detailsDoc = db.collection(collectionOrder).document(orderDoc.id)
                .collection(collectionOrderDetails).document(it.productId!!)

            //Now in this document we will write on this order details

            //write batch will set this model into this doc
            //just put into the queue
            wb.set(detailsDoc, it)

        }
        //If any one is failed, it will show the error
        wb.commit()
            .addOnSuccessListener {
              // If everything will be successful call this callback
                callback(DBResult.SUCCESS)

            }.addOnFailureListener {
               callback(DBResult.FAILURE)
            }





    }
}