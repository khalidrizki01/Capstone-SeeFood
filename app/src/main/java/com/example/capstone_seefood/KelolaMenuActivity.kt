package com.example.capstone_seefood

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KelolaMenuActivity : AppCompatActivity() {
    private lateinit var rvMenuTersedia : RecyclerView
    private lateinit var rvTambahkanMenu : RecyclerView
    private lateinit var foodDao : FoodDao
    private val listMenuTersedia = ArrayList<Food>()
    private val listTambahkanMenu = ArrayList<Food>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelola_menu)
        foodDao = FoodDatabase.getInstance(this).foodDao

        rvMenuTersedia = findViewById(R.id.rvMenuTersedia)
        rvMenuTersedia.setHasFixedSize(true)
        rvTambahkanMenu = findViewById(R.id.rvTambahkanMenu)
        rvTambahkanMenu.setHasFixedSize(true)

        GlobalScope.launch {
            listMenuTersedia.addAll(foodDao.getAvailableFood())
            listTambahkanMenu.addAll(foodDao.getUnavailableFood())
        }
    }
    fun getDrawableResourceId(resourceName: String): Int {
        val resources = resources
        return resources.getIdentifier(resourceName, "drawable", packageName)
    }

    // Menggunakan resource ID untuk mendapatkan Drawable
    fun getDrawableFromResource(resourceName: String): Drawable? {
        val resourceId = getDrawableResourceId(resourceName)
        return if (resourceId != 0) {
            ContextCompat.getDrawable(this, resourceId)
        } else {
            null
        }
    }
}