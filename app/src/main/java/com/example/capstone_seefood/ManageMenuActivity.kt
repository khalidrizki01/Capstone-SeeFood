package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.ui.menu.FragmentMenu

class ManageMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)

        val fragmentManager = supportFragmentManager
        val menuFragment = FragmentMenu()
        val fragment = fragmentManager.findFragmentByTag(FragmentMenu::class.java.simpleName)

        if(fragment !is FragmentMenu){
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, menuFragment, FragmentMenu::class.java.simpleName)
                .commit()
        }
    }
}