package com.example.capstone_seefood

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        initData()
        GlobalScope.launch {
            foodDao.deleteAllFood()
            foodDao.deleteAllReceipts()
            foodDao.deleteAllReceiptFoodCR()
            foods.forEach{foodDao.insertFood(it)}
            val foodInDB = foodDao.getFoodBasedOnName("Ayam Goreng")
            GlobalScope.launch(Dispatchers.Main) {
                binding.imgOrder.setImageBitmap(foodInDB.photo)
            }
//            storeReceipt()
//            receipts.forEach { foodDao.insertReceipt(it) }
//            receiptFoodRelations.forEach { foodDao.insertReceiptFoodCrossRef(it) }
//            val receiptWithFoods = foodDao.getReceiptWithFoods(receipt1Id)
        }
    }



    private fun initData() {
//        val listImage = listOf("ayamgoreng", "nasi", "sambal", "tahu", "tempe")
//        val listHarga = listOf(9000, 3000, 0, 1000, 700)
//        listImage.forEach()
//        val ayam = resources.getIdentifier("ayamgoreng", "drawable", packageName )
//        val ayamid = resources.getIdentifier("ayamgoreng", "drawable", packageName)
//        val nasiid = resources.getIdentifier("nasi", "drawable", packageName)
//        val sambalid = resources.getIdentifier("sambal", "drawable", packageName
        val ayamid = resources.getIdentifier("ayamgoreng", "drawable", packageName)
//        val ayamGoreng = resources.getDrawable(R.drawable.ayamgoreng)
        val bitmap = BitmapFactory.decodeResource(resources, ayamid)

//        val ayamGorengByteArray = ayamGoreng.toBy

//        val drawayam = resources.getDrawable(ayamid, null)
//        val drawnasi = resources.getDrawable(nasiid, null)
//        val drawasambal = resources.getDrawable(sambalid, null)

        val food1Id = UUID.randomUUID()
        val food2Id = UUID.randomUUID()
        val food3Id = UUID.randomUUID()
        foods = listOf(
            Food(food1Id, "Ayam Goreng", bitmap, 12000, true),
//            Food(food2Id, "Nasi", 2, 7000, true),
//            Food(food3Id, "Sambal", 3),
        )
    }

    private fun storeReceipt() {
        foods.forEach { foodDao.insertFood(it) }

        identifiedFoods = listOf("Nasi Goreng", "Telur")
        foodQuantities = listOf(1, 2)

        var recId : UUID = UUID.randomUUID()
        var totalPrice : Int = 0
        for((index, identifiedFood) in identifiedFoods.withIndex()) {
            var oneFood = foodDao.getFoodBasedOnName(identifiedFood)
            if(oneFood.isSell) {
                var receiptFood = ReceiptFoodCrossRef(recId, oneFood.foodId, foodQuantities[index])
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
}