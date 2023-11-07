package com.example.capstone_seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
//    private lateinit var btnConfirmPayment : Button


    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        db = Room.databaseBuilder(applicationContext, FoodDatabase::class.java, "food-db").build()
        val foodDao = FoodDatabase.getInstance(this).foodDao
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

//        binding.btnConfirmPayment.setOnClickListener {
//            goToReceiptActivity()
//        }
        val food1Id = UUID.randomUUID()
        val food2Id = UUID.randomUUID()
        val food3Id = UUID.randomUUID()

        val receipt1Id = UUID.randomUUID()
        val receipt2Id = UUID.randomUUID()
        val foods = listOf(
            Food(food1Id, "Nasi Goreng", 12000, 1, true),
            Food(food2Id, "Mie Goreng", 7000, 2, true),
            Food(food3Id, "Telur", 5000, 3, true)
        )

        val receipts = listOf(
            Receipt(receipt1Id, 27000, 29700),
            Receipt(receipt2Id, 27000, 29700)
        )
        val receiptFoodRelations = listOf(
            ReceiptFoodCrossRef(receipt1Id, food1Id, 2, 24000),
            ReceiptFoodCrossRef(receipt1Id, food3Id, 1, 14000),
            ReceiptFoodCrossRef(receipt2Id, food2Id, 1, 12000),
            ReceiptFoodCrossRef(receipt2Id, food3Id, 3, 15000)
        )
        GlobalScope.launch {
            foods.forEach { foodDao.insertFood(it) }
            receipts.forEach { foodDao.insertReceipt(it) }
            receiptFoodRelations.forEach { foodDao.insertReceiptFoodCrossRef(it) }
            val receiptWithFoods = foodDao.getReceiptWithFoods(receipt1Id)
        }
    }

    private fun goToReceiptActivity() {
        TODO("Not yet implemented")
    }
}