package com.example.capstone_seefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import com.example.capstone_seefood.databinding.ActivityReceiptBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class ReceiptActivity : AppCompatActivity(){
    private lateinit var binding: ActivityReceiptBinding
    private lateinit var btnReceipt : Button
    private lateinit var foodDao : FoodDao
    //    lateinit var db : FoodDatabase

    companion object {
        const val TAG = "ReceiptActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        foodDao = FoodDatabase.getInstance(this).foodDao
        super.onCreate(savedInstanceState)

        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val confirmedReceiptId = intent.getSerializableExtra("confirmedReceiptId") as UUID
        Log.d(TAG, "RECEIVED : ${confirmedReceiptId!!}")

        var result : List<ReceiptFoodCrossRef>

        GlobalScope.launch{
            result = foodDao.getReceiptFoodCrossRefByReceiptId(confirmedReceiptId)
            Log.d(TAG, result.toString())
            var count: Int = 1
            for(item in result) {
                Log.d(TAG, item.foodName)
                val tableRow = TableRow(this@ReceiptActivity)

                // Kolom 1: Food Name
                val foodNameTextView = TextView(this@ReceiptActivity)
                foodNameTextView.text = item.foodName
                tableRow.addView(foodNameTextView)

                // Kolom 2: Quantity
                val quantityTextView = TextView(this@ReceiptActivity)
                quantityTextView.text = item.quantity.toString()
                tableRow.addView(quantityTextView)

                // Kolom 3: Food Price
                val foodPriceTextView = TextView(this@ReceiptActivity)
                foodPriceTextView.text = item.foodPrice?.toString() ?: "N/A"
                tableRow.addView(foodPriceTextView)

                // Kolom 4: Total Item Price
                val totalItemPriceTextView = TextView(this@ReceiptActivity)
                totalItemPriceTextView.text = item.totalItemPrice?.toString() ?: "N/A"
                tableRow.addView(totalItemPriceTextView)

                GlobalScope.launch(Dispatchers.Main){
                    binding.tbOrder.addView(tableRow, count)
                    count++
                }
            }
        }

        binding.btnKembali.setOnClickListener {
            val intent = Intent(this@ReceiptActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}