package com.example.chatappw6_doan.utils

import com.google.firebase.auth.FirebaseAuth

class Util {
    private var firebaseAuth : FirebaseAuth? = null

    public fun getUid() : String{
        firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth!!.uid.toString()
    }
}