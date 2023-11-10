package com.example.capstone_seefood

import android.content.Context
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
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.util.mlTransform
//import com.google.flatbuffers.Table
import java.io.File

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var foods : List<Food>
    private lateinit var identifiedFoods : List<String>
    private lateinit var foodQuantities : List<Int>
    private lateinit var foodDao : FoodDao
    private lateinit var scannedBitmap : Bitmap
    private lateinit var listOrderedFood : List<IdentifiedFoodAndConf>

    companion object {
        const val PICTURE = "picture"
    }


//    private lateinit var btnConfirmPayment : Button


    val data = listOf(
        listOf("Nasi Goreng", "2", "Rp 15,000", "Rp 30,000"),
        listOf("Mie Goreng", "1", "Rp 12,000", "Rp 12,000"),
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("IDENTIFY", "Berhasil ke identify food activity")
        super.onCreate(savedInstanceState)

        foodDao = FoodDatabase.getInstance(this).foodDao
        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = intent.getSerializableExtra(PICTURE) as File
        val result = BitmapFactory.decodeFile(myFile.path)
        mlTransform(result, this)
//        val predictResult = mlTransform(result, this)


//        listOrderedFood = mlTransform(scannedBitmap, this)
//        for ((name, conf) in listOrderedFood) {
//            val tableRow = TableRow(this)
//            val textView = TextView(this)
//            textView.text = name
//            textView.layoutParams = TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT
//            )
//            tableRow.addView(textView)
//            binding.tbOrder.addView(tableRow)
//        }


//        GlobalScope.launch {
//            var recId = UUID.randomUUID()
//            for ((name, conf) in listOrderedFood) {
//                var foodItem = foodDao.getFoodBasedOnName(name)
//                foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, foodItem.foodId, foodItem.name,foodItem.price, 1))
//            }
//            for
//        }
//        binding.btnConfirmPayment.setOnClickListener {
//            goToReceiptActivity()
//        }
    }

    private fun goToReceiptActivity() {
        TODO("Not yet implemented")
    }

    data class IdentifiedFoodAndConf (
        val foodName : String,
        val confidence : Float
    )

    private fun getIdentifiedFoods(foodLabels : List<String>, confidenceArr : FloatArray) : List<IdentifiedFoodAndConf>{
        var identifiedList : MutableList<IdentifiedFoodAndConf> = mutableListOf()
        for((index, conf) in confidenceArr.withIndex()){
            if(conf > 0.5) {
                identifiedList.add(IdentifiedFoodAndConf(foodLabels[index], conf))
            }
        }
        return identifiedList
    }

}