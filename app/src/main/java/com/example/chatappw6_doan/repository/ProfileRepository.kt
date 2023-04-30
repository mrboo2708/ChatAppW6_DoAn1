package com.example.chatappw6_doan.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatappw6_doan.model.ResponseUser
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.utils.Util
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfileRepository {

    private var database : FirebaseFirestore? = null
    private var util = Util()
//    private var liveData: MutableLiveData<UserModel>? = null
    private var profileRepository : ProfileRepository? = null

//    fun getUser() : LiveData<UserModel> {
//        if(liveData == null){
//            liveData = MutableLiveData()
//            database = Firebase.firestore
//            Log.d("check", "inside getuser for")
//            val docRef = database!!.collection("Users").document(util!!.getUid())
//            Log.d("check", "DocumentSnapshot data: ${util!!.getUid()}")
//            docRef.get().addOnSuccessListener {
//                if (it != null) {
//                    Log.d("check", "DocumentSnapshot data: ${it.data}")
//                    var userModel = it.toObject<UserModel>()
//                    if(userModel!= null){
//                        liveData!!.value = userModel
//                    }
//
//
//
//
//                } else {
//                    Log.d("check", "No such document")
//                }
//            }.addOnFailureListener {
//                Log.d("check", "Fail")
//            }
//        }
//        return liveData as MutableLiveData<UserModel>
//
//    }

    fun getUserResponse() : MutableLiveData<ResponseUser>{
        database = Firebase.firestore
        val mutableLiveData = MutableLiveData<ResponseUser>()
        val docRef = database!!.collection("Users").document(util!!.getUid())
        docRef.get().addOnCompleteListener { task ->
            val response = ResponseUser()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.userModel = it.toObject(UserModel::class.java)


                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
        }
        return mutableLiveData
    }



    fun getInstance() : ProfileRepository {
        return ProfileRepository().also { profileRepository = it }
    }


}