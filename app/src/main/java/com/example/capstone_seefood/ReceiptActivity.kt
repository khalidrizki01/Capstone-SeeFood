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

class ReceiptActivity : AppCompatActivity(){
    private lateinit var binding: ActivityReceiptBinding
    private lateinit var btnReceipt : Button
    private lateinit var foodDao : FoodDao
    //    lateinit var db : FoodDatabase

    companion object {
        const val ORDERED_FOODS = "ordered_foods"
    }

//    val data = listOf(
//        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
//        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
//        // Tambahkan data lainnya sesuai kebutuhan
//    )
    override fun onCreate(savedInstanceState: Bundle?) {
        foodDao = FoodDatabase.getInstance(this).foodDao
        super.onCreate(savedInstanceState)

        //db = Room.databaseBuilder(applicationContext, FoodDatabase::class.java, "food-db").build()

        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val orderId = intent.getStringExtra("ORDER_ID")
        val newOrderId = orderId!!.replace("-", "")

        Log.d("RECEIPT", "RECEIVED : ${orderId!!}")
        Log.d("RECEIPT", newOrderId)
        var result : List<ReceiptFoodCrossRef>

        GlobalScope.launch{
            result = foodDao.getAllReceiptFoodCrossRef()
            Log.d("RECEIPT", result.toString())
            var count: Int = 1
            for(item in result) {
                Log.d("RECEIPT", item.foodName)
                val tableRow = TableRow(this@ReceiptActivity)

                // Kolom 1: Food Name
                val foodNameTextView = TextView(this@ReceiptActivity)
                foodNameTextView.text = item.foodName
                tableRow.addView(foodNameTextView)



                // Kolom 3: Quantity
                val quantityTextView = TextView(this@ReceiptActivity)
                quantityTextView.text = item.quantity.toString()
                tableRow.addView(quantityTextView)

                // Kolom 2: Food Price
                val foodPriceTextView = TextView(this@ReceiptActivity)
                foodPriceTextView.text = item.foodPrice?.toString() ?: "N/A"
                tableRow.addView(foodPriceTextView)

                // Kolom 4: Total Item Price
                val totalItemPriceTextView = TextView(this@ReceiptActivity)
                totalItemPriceTextView.text = item.totalItemPrice?.toString() ?: "N/A"
                tableRow.addView(totalItemPriceTextView)
                    // Implementasikan di sini
                GlobalScope.launch(Dispatchers.Main){
                    binding.tbOrder.addView(tableRow, count)
                    count++
                }
            }
        }


//        val oneReceipt = result[0]
//        var count: Int = 1
//        for (rowData in data) {
//            val tableRow = TableRow(this)
//            for (item in rowData) {
//                val textView = TextView(this)
//                textView.text = item
//                textView.gravity = Gravity.START
//                textView.setPadding(5, 5, 5, 5)
//                tableRow.addView(textView)
//            }
//            binding.tbOrder.addView(tableRow, count)
//            count++
////        }
//        GlobalScope.launch {
//            initData()
//        }
//        binding.btnReceipt.setOnClickListener(this)
//        }
        binding.btnKembali.setOnClickListener {
            val intent = Intent(this@ReceiptActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}