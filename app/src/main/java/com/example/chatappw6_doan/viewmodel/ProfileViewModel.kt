package com.example.chatappw6_doan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatappw6_doan.model.ResponseUser
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.repository.ProfileRepository

class ProfileViewModel : ViewModel() {
    private var profileRepository : ProfileRepository = ProfileRepository().getInstance()

    fun getResponseUser() = profileRepository.getUser()
    fun editImage(uri: String) {
        profileRepository.editImage(uri)


    }
    fun editStatus(status: String?) {
        profileRepository.editStatus(status)

    }

    fun editUserName(name: String?) {
        profileRepository.editUserName(name)

    }

}