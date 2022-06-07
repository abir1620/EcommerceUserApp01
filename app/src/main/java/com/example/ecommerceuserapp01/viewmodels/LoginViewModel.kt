package com.example.ecommerceuserapp01.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerceuserapp01.models.EcomUser
import com.example.ecommerceuserapp01.repositories.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class LoginViewModel: ViewModel() {

    enum class Authentication{
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authLD: MutableLiveData<Authentication> = MutableLiveData()
    val errorMsgLD: MutableLiveData<String> = MutableLiveData()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val userRepository= UserRepository()


    init {
        if(auth.currentUser != null){
            authLD.value = Authentication.AUTHENTICATED
        }else{
            authLD.value = Authentication.UNAUTHENTICATED
        }
    }

    fun registerUser(name: String, mobile: String, email:String, password: String, confirmPass: String  ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val ecomUser = EcomUser(
                    userId = it.user?.uid,
                    userName=name,
                    userMobile=mobile,
                    userEmail = it.user?.email,
                    password = password,
                    confirmPassword=confirmPass,
                    userCreationDate = Timestamp(Date(it.user?.metadata?.creationTimestamp!!)),
                    userLastSignInTimestamp = Timestamp(Date(it.user?.metadata?.lastSignInTimestamp!!))
                )
                userRepository.addNewUser(ecomUser)
                authLD.value=Authentication.AUTHENTICATED
            }.addOnFailureListener {
                errorMsgLD.value = it.localizedMessage
            }

    }

    fun loginUser(email: String, pass: String){
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                db.collection("Users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener {
                        if(it.exists()){
                            authLD.value = Authentication.AUTHENTICATED
                        }else{
                            errorMsgLD.value = "User name or password wrong"
                            auth.signOut()
                        }
                    }.addOnFailureListener {
                        errorMsgLD.value = "User name or Password wrong"
                        auth.signOut()
                    }
            }.addOnFailureListener {
                errorMsgLD.value=it.localizedMessage
            }
    }

    fun updateAppExitTimeAndAvailableStatus(status: Boolean, time: Timestamp){
        userRepository.updateAppExitTimeAndAvailableStatus(
            userId =  auth.currentUser!!.uid,
            status = status,
            time = time
        )
    }
    //now this method will call by main activity onStop() method

    fun updateAvailableStatus(status: Boolean){
        //we will call the repository updateAvailableStatus()
        userRepository.updateAvailableStatus(
            userId = auth.currentUser!!.uid,
            status = status
        )
    }
    //This method will call when user enter the app except login

    fun logout(){
        //First we will update the database and then log out
        userRepository.updateAppExitTimeAndAvailableStatus(
            userId = auth.currentUser!!.uid,
            time = Timestamp(Date(System.currentTimeMillis())),
            status = false
        //need a callback method
        ){
            //write on the database then I will call the signout method
            auth.signOut()
           authLD.value =Authentication.UNAUTHENTICATED
        }
    }


}