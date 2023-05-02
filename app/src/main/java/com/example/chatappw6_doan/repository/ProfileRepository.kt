package com.example.chatappw6_doan.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatappw6_doan.model.ResponseUser
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.utils.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileRepository {

    private var database : FirebaseFirestore? = null
    private var util = Util()
    private var liveData: MutableLiveData<UserModel>? = null
    private var profileRepository : ProfileRepository? = null



    fun getUser(): LiveData<UserModel?>? {
        if (liveData == null) {
            database = Firebase.firestore
            liveData = MutableLiveData<UserModel>()
            val docRef = database!!.collection("Users").document(util!!.getUid())
            docRef.get().addOnCompleteListener { task ->
                var userModel = UserModel()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        userModel = it.toObject(UserModel::class.java)!!


                    }
                }
                liveData!!.value = userModel
            }

        }
        return liveData
    }



    fun getInstance() : ProfileRepository {
        return ProfileRepository().also { profileRepository = it }
    }

    fun editImage(uri: String) {
        val userModel = liveData!!.value
        userModel!!.image = uri
        database = Firebase.firestore
        val docRef = database!!.collection("Users").document(util!!.getUid())
        val map: MutableMap<String, Any> = HashMap()
        map["image"] = uri
        docRef.update(map).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("image","image update")
                liveData!!.value = userModel
            }
            else Log.d("image",""+ it.exception)
        }
    }

    fun editStatus(status: String?) {
        val userModel = liveData!!.value
        userModel!!.status = status
        database = Firebase.firestore
        val docRef = database!!.collection("Users").document(util!!.getUid())
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["status"] = status!!
        docRef.update(map).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("image","image update")
                liveData!!.value = userModel
            }
            else Log.d("image",""+ it.exception)
        }
    }

    fun editUserName(name: String?) {
        val userModel = liveData!!.value
        userModel!!.name = name
        database = Firebase.firestore
        val docRef = database!!.collection("Users").document(util!!.getUid())
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["name"] = name!!
        docRef.update(map).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("name","name update")
                liveData!!.value = userModel
            }
            else Log.d("name",""+ it.exception)
        }
    }


}