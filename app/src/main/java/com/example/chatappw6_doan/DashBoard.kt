package com.example.chatappw6_doan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatappw6_doan.fragment.ChatFragment
import com.example.chatappw6_doan.fragment.ContactFragment
import com.example.chatappw6_doan.fragment.ProfileFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class DashBoard : AppCompatActivity() {

    private var navigationBar: ChipNavigationBar? = null
    private var fragment : Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        navigationBar = findViewById(R.id.navigationChip)
        if(savedInstanceState == null){
            val chatFragment = ChatFragment()
            navigationBar.let {
                it?.setItemSelected(R.id.chat,true)
            }
            supportFragmentManager.beginTransaction().replace(R.id.dashboardContainer,chatFragment).commit()
        }
        navigationBar.let {
            it!!.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
                override fun onItemSelected(i: Int) {
                    when(i){
                        R.id.chat -> {
                            fragment = ChatFragment()
                        }
                        R.id.contacts -> {
                            fragment = ContactFragment()
                        }
                        R.id.profile -> {
                            fragment = ProfileFragment()
                        }

                    }
                    if(fragment != null){
                        supportFragmentManager.beginTransaction().replace(R.id.dashboardContainer,
                            fragment!!
                        ).commit()
                    }
                }
            })
        }

    }
}