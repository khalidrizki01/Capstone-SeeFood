package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.room.Room
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var btnConfirmPayment : Button
    lateinit var db : FoodDatabase

    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, FoodDatabase::class.java, "food-db").build()

        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var count: Int = 1
        for (rowData in data) {
            val tableRow = TableRow(this)
            for (item in rowData) {
                val textView = TextView(this)
                textView.text = item
                textView.gravity = Gravity.START
                textView.setPadding(5, 5, 5, 5)
                tableRow.addView(textView)
            }
            binding.tbOrder.addView(tableRow, count)
            count++
        }
        GlobalScope.launch {
            deleteData()
            initData()
        }


//        binding.btnConfirmPayment.setOnClickListener(this)
    }

    private fun deleteData() {

    }
    private fun initData() {
        val food1 = Food("Nasi Goreng", 2, 15000, true)
        val food2 = Food("Mie Goreng", 1, 12,true)

        //insert data ke database
        db.foodDao().insert(food1)
        db.foodDao().insert(food2)
    }
}