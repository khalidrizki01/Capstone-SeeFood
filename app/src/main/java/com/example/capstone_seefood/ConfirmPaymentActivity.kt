package com.example.capstone_seefood

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
//import coil.ImageLoader
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.FoodSum
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var foods : List<Food>
    private lateinit var identifiedFoods : List<String>
    private lateinit var foodQuantities : List<Int>
    private lateinit var foodDao : FoodDao

//    private lateinit var btnConfirmPayment : Button


    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        foodDao = FoodDatabase.getInstance(this).foodDao
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
//        initData()
        GlobalScope.launch {
//            foodDao.deleteAllFood()
//            foodDao.deleteAllReceipts()
//            foodDao.deleteAllReceiptFoodCR()
//            foods.forEach{foodDao.insertFood(it)}
            val foodInDB = foodDao.getFoodBasedOnName("Nasi")
            val ayamid = resources.getIdentifier(foodInDB.photo, "drawable", packageName)
            GlobalScope.launch(Dispatchers.Main) {
                binding.imgOrder.setImageResource(ayamid)
            }
            storeReceipt()
            val listPenjualan = getSalesThisMonth()
            for((name, total) in listPenjualan) {
                Log.d("DB", "${name} : ${total}")
            }
//            receipts.forEach { foodDao.insertReceipt(it) }
//            receiptFoodRelations.forEach { foodDao.insertReceiptFoodCrossRef(it) }
//            val receiptWithFoods = foodDao.getReceiptWithFoods(receipt1Id)
        }
    }



    private fun initData() {
        val ayamid = resources.getIdentifier("ayamgoreng", "drawable", packageName)
        Log.d("DB", "${ayamid.toString()}")
        val food1Id = UUID.randomUUID()
        val food2Id = UUID.randomUUID()
        val food3Id = UUID.randomUUID()
        Log.d("DB", food1Id.toString())
        Log.d("DB", food2Id.toString())
        foods = listOf(
            Food(1, "Ayam Goreng", "ayamgoreng", 12000, true),
//            Food(food2Id, "Nasi", 2, 7000, true),
//            Food(food3Id, "Sambal", 3),
        )
    }

    private suspend fun storeReceipt() {
//        foods.forEach { foodDao.insertFood(it) }

        identifiedFoods = listOf("Nasi")
        foodQuantities = listOf(2)

        var recId : UUID = UUID.randomUUID()
        var totalPrice : Int = 0
        for((index, identifiedFood) in identifiedFoods.withIndex()) {
            var oneFood = foodDao.getFoodBasedOnName(identifiedFood)
            if(oneFood.isSell) {
                var receiptFood = ReceiptFoodCrossRef(recId, oneFood.foodId, oneFood.name, oneFood.price,foodQuantities[index])
                totalPrice += receiptFood.calculateTotalItemPrice(oneFood.price!!)
                foodDao.insertReceiptFoodCrossRef(receiptFood)
            } else {
                continue
            }
        }
        var newReceipt = Receipt(recId, totalPrice)
        foodDao.insertReceipt(newReceipt)
    }

    private fun goToReceiptActivity() {
        TODO("Not yet implemented")
    }
    private suspend fun getSalesThisMonth() : List<FoodSum>{
        val startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay()
        return foodDao.getTotalItemPricePerFoodId(startOfMonth)
    }
}