package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone_seefood.ui.manageprice.ManagePriceFragment

class manage_price : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_price)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .commitNow()
        }
    }
}