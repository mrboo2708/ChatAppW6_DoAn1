package com.example.chatappw6_doan.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatappw6_doan.R
import com.example.chatappw6_doan.fragment.GetNumberFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val fragment = GetNumberFragment()
        ft.add(R.id.container, fragment).commit()

    }

}