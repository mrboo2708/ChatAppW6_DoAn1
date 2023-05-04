package com.example.chatappw6_doan.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chatappw6_doan.R
import com.example.chatappw6_doan.databinding.ActivityUserInfoBinding
import com.example.chatappw6_doan.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserInfo : AppCompatActivity() {
    private var binding : ActivityUserInfoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val uId = intent.extras
        getUserDetail(uId!!.getString("userID")!!)
        Log.d("check", intent.hasExtra("userID").toString())
    }

    private fun getUserDetail(uId: String) {
        val database = Firebase.firestore.collection("Users").document(uId)
        database.get().addOnCompleteListener{
            if(it.isSuccessful){
                var userModel = UserModel()
                userModel = it.result.toObject(UserModel::class.java)!!
                userModel.uID = it.result.id
                binding!!.userModel = userModel
                Log.d("check", userModel.uID!!)
                if (userModel.name!!.contains(" ")) {
                    val split: Array<String> = userModel.name!!.split(" ").toTypedArray()
                    binding!!.txtProfileFName.text = split[0]
                    binding!!.txtProfileLName.text = split[1]
                } else {
                    binding!!.txtProfileFName.setText(userModel.name)
                    binding!!.txtProfileLName.text = ""
                }
            }
        }

    }
}