package com.example.chatappw6_doan.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.repository.ProfileRepository

class ProfileViewModel : ViewModel() {
    private var userModelMutableLiveData : MutableLiveData<UserModel> = MutableLiveData()
    private var profileRepository : ProfileRepository = ProfileRepository().getInstance()

//    fun getUser() : LiveData<UserModel>{
//        Log.d("Check","get user inside")
//        return profileRepository.getUser()
//    }
    fun getResponseUser() = profileRepository.getUserResponse()

}