package com.example.capstone_seefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableRow
import com.example.capstone_seefood.databinding.ActivityReceiptBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.example.capstone_seefood.util.addTextViewToTableRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.UUID
import com.example.capstone_seefood.util.formatNumber

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

        var crossRefs : List<ReceiptFoodCrossRef>
        var receipt : Receipt

        GlobalScope.launch{
            receipt = foodDao.getOneReceipt(confirmedReceiptId)
            crossRefs = foodDao.getReceiptFoodCrossRefByReceiptId(confirmedReceiptId)
            Log.d(TAG, crossRefs.toString())
            var count: Int = 1
            for((index, item) in crossRefs.withIndex()) {
                Log.d(TAG, item.foodName)
                val tableRow = TableRow(this@ReceiptActivity)

                // Kolom 1: Food Name
//                val foodNameTextView = TextView(this@ReceiptActivity)
//                foodNameTextView.text = item.foodName
//                tableRow.addView(foodNameTextView)

//                // Kolom 2: Quantity
//                val quantityTextView = TextView(this@ReceiptActivity)
//                quantityTextView.text = item.quantity.toString()
//                tableRow.addView(quantityTextView)
//
//                // Kolom 3: Food Price
//                val foodPriceTextView = TextView(this@ReceiptActivity)
//                foodPriceTextView.text = item.foodPrice?.toString() ?: "N/A"
//                tableRow.addView(foodPriceTextView)
//
//                // Kolom 4: Total Item Price
//                val totalItemPriceTextView = TextView(this@ReceiptActivity)
//                totalItemPriceTextView.text = item.totalItemPrice?.toString() ?: "N/A"
//                tableRow.addView(totalItemPriceTextView)

                addTextViewToTableRow(this@ReceiptActivity, tableRow, item.foodName)
                addTextViewToTableRow(this@ReceiptActivity, tableRow, item.quantity.toString())
                addTextViewToTableRow(this@ReceiptActivity, tableRow, "Rp${formatNumber(item.foodPrice!!)}")
                addTextViewToTableRow(this@ReceiptActivity, tableRow, "Rp${formatNumber(item.totalItemPrice!!)}")

                GlobalScope.launch(Dispatchers.Main){
                    binding.tbOrder.addView(tableRow, count)
                    count++

                    if (index == crossRefs.size - 1) {
                        // Jika ini adalah iterasi terakhir, maka tampilkan total harga ke table layout di xml
                        binding.tvReceiptId.text = receipt.receiptId.toString()
                        var totalPrice = formatNumber(receipt.totalPrice)
                        binding.tvTotalTransaksi.text = "Rp${totalPrice}"
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        binding.tvTanggalTransaksi.text = receipt.createdAt.format(formatter).toString()
                    }
                }
            }
        }

        binding.btnKembali.setOnClickListener {
            val intent = Intent(this@ReceiptActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}