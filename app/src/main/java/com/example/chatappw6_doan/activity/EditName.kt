package com.example.chatappw6_doan.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatappw6_doan.constants.AllConstants
import com.example.chatappw6_doan.databinding.ActivityEditNameBinding

class EditName : AppCompatActivity() {
    private var binding : ActivityEditNameBinding? = null
    private var firstName : String? = null
    private var lastName : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNameBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        var name = intent.getStringExtra("name")
        if (name!!.contains(" ")) {
            val split = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            binding!!.edtFName.setText(split[0])
            binding!!.edtLName.setText(split[1])
        } else {
            binding!!.edtFName.setText(name)
            binding!!.edtLName.setText("")
        }

        binding!!.btnEditName.setOnClickListener {
            if(checkFirstName() && checkLastName()){
                val intent = Intent()
                intent.putExtra("name",firstName + " " + lastName )
                setResult(AllConstants.USERNAME_CODE,intent)
                finish()
            }
        }

    }

    private fun checkFirstName() : Boolean {
        firstName = binding!!.edtFName.text.toString().trim()
        if(firstName!!.isEmpty()){
            binding!!.edtFName.setError("Filed is required")
            return false
        }
        else {
            binding!!.edtFName.error = null
            return true
        }
    }

    private fun checkLastName() : Boolean {
        lastName = binding!!.edtLName.text.toString().trim()
        if(lastName!!.isEmpty()){
            binding!!.edtLName.setError("Filed is required")
            return false
        }
        else {
            binding!!.edtLName.error = null
            return true
        }
    }



}