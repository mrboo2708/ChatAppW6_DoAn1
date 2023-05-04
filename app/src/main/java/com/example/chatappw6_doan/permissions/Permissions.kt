package com.example.chatappw6_doan.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chatappw6_doan.constants.AllConstants

class Permissions {
    fun isStorageOk(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestStorage(activity: Activity?) {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf<String>(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            AllConstants.STORAGE_REQUEST_CODE
        )
    }

    fun isContactOk(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestContact(activity: Activity?) {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf<String>(
                Manifest.permission.READ_CONTACTS,
            ),
            AllConstants.CONTACTS_REQUEST_CODE
        )
    }

}