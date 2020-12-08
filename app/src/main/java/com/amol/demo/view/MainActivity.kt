package com.amol.demo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.amol.demo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragmentToActivity(UsersListFragment(), supportFragmentManager)
    }

    fun addFragmentToActivity(fragment: Fragment, manager: FragmentManager) {
        val transaction: FragmentTransaction =
            manager.beginTransaction().addToBackStack(fragment.tag)
        transaction.add(R.id.app_main_fragment_container, fragment)
        transaction.commit()
    }
}