package com.example.chatappw6_doan.model

import android.R.attr.author


data class dataUser(var name : String,var status : String,var image : String) {

    fun toMap(): Map<String, Any>? {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["status"] = status
        result["image"] = image
        return result
    }
}