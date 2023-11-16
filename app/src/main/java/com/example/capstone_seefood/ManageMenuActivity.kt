package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.ui.menu.FragmentMenuWithEmptyCardView

class ManageMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)

        val fragmentManager = supportFragmentManager
        val menuFragment = FragmentMenuWithEmptyCardView()
        val fragment = fragmentManager.findFragmentByTag(FragmentMenuWithEmptyCardView::class.java.simpleName)

        if(fragment !is FragmentMenuWithEmptyCardView){
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, menuFragment, FragmentMenuWithEmptyCardView::class.java.simpleName)
                .commit()
        }
    }

}