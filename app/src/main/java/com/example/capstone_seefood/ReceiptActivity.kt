package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import com.example.capstone_seefood.databinding.ActivityReceiptBinding

abstract class ReceiptActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityReceiptBinding
    private lateinit var btnReceipt : Button
    //    lateinit var db : FoodDatabase

    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //db = Room.databaseBuilder(applicationContext, FoodDatabase::class.java, "food-db").build()

        binding = ActivityReceiptBinding.inflate(layoutInflater)
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
//        }
//        GlobalScope.launch {
//            initData()
//        }
//        binding.btnReceipt.setOnClickListener(this)
        }

    }
}