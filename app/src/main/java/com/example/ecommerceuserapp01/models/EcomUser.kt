package com.example.ecommerceuserapp01.models

import com.google.firebase.Timestamp

data class EcomUser(
    var userId: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userMobile: String? = null,
    var userAddress: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var userImage: String? = null,
     var userCreationDate: Timestamp? = null,
    var userLastSignInTimestamp: Timestamp? = null,
    var available : Boolean = true,
    var lastUserAppExitTime: Timestamp? = null

   //add mobile


)
