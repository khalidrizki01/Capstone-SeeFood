package com.example.capstone_seefood

//import com.google.flatbuffers.Table

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_seefood.api.ApiConfig
import com.example.capstone_seefood.api.IdentifiedFood
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.TempFood
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.UUID


class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var foodDao : FoodDao
    private lateinit var listOrderedFood : List<IdentifiedFood>
    private lateinit var toStoreFoods : MutableList<TempFood>
    private val apiService = ApiConfig.getApiService()
    private lateinit var recId : UUID
    private lateinit var stored : UUID
    private var totalPrice = 0
    private val TAG: String = "CHECK_RESPONSE"

    companion object {
        const val PICTURE = "picture"
        private const val TAG = "ConfirmPaymentActivity"
    }


//    private lateinit var btnConfirmPayment : Button


    val data = listOf(
        listOf("Nasi", "1", "Rp3.000", "Rp3.000"),
        listOf("Ayam Goreng", "1", "8.000", "Rp8.000"),
        listOf("Tahu", "1", "Rp1.000", "Rp1.000"),
        listOf("Tempe", "2", "Rp700", "Rp1.400")
        // Tambahkan data lainnya sesuai kebutuhan
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        toStoreFoods = mutableListOf()
        Log.d("IDENTIFY", "Berhasil ke identify food activity")
        super.onCreate(savedInstanceState)

        foodDao = FoodDatabase.getInstance(this).foodDao
        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = intent.getSerializableExtra(PICTURE) as File
        Log.d("PHOTO", myFile.path.toString())
        val requestFile = myFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            myFile.name,
            requestFile
        )

//        val call = apiService.uploadImage(image)
//        call.enqueue(object : Callback<ResultModel> {
//            override fun onResponse(
//                call: Call<ResultModel>,
//                response: Response<ResultModel>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("RESPONS", "API berhasil")
//                    val result = response.body()
//
//                    listOrderedFood = result?.labels ?: emptyList()
//
//
//                    for ((index, food) in listOrderedFood.withIndex()) {
//                        GlobalScope.launch {
//                            var foodItem = foodDao.getFoodBasedOnName(food.label)
//                            val tableRow = TableRow(this@ConfirmPaymentActivity)
//                            if (foodItem.isSell) {
//                                val totalItemPrice = food.count * foodItem.price!!
//                                totalPrice += totalItemPrice
//                                toStoreFoods.add(TempFood(foodItem.foodId, foodItem.name, food.count, foodItem.price!!, totalItemPrice))
//                                Log.d("DETEKSI", "hasil: ${toStoreFoods[index].foodId} | ${toStoreFoods[index].name} | ${toStoreFoods[index].price} | ${toStoreFoods[index].quantity} | ${toStoreFoods[index].totalItemPrice}")
//
//                                addTextViewToTableRow(tableRow, foodItem.name)
//                                addTextViewToTableRow(tableRow, food.count.toString())
//                                addTextViewToTableRow(tableRow, foodItem.price.toString())
//                                addTextViewToTableRow(tableRow, totalItemPrice.toString())
//                            }
//                            GlobalScope.launch(Dispatchers.Main) {
//                                binding.tbOrder.addView((tableRow))
//                                binding.tvTotalHarga.text = totalPrice.toString()
//                            }
//                        foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, foodItem.foodId, foodItem.name,foodItem.price, food.count))
//                        }
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
//                // Handle jika ada kesalahan pada permintaan
//                Log.i(TAG, "onFailure : ${t.message}")
//            }
//        })
        val bitmap = BitmapFactory.decodeFile(myFile.path)
        binding.imgOrder.setImageBitmap(bitmap)

        // Set gambar ke ImageView
        binding.imgOrder.setImageBitmap(bitmap)
        var count: Int = 1
        for ((index, rowData) in data.withIndex()) {
            val tableRow = TableRow(this)
            for ((i, item) in rowData.withIndex()) {
                val textView = TextView(this)
                textView.text = item
                textView.setPadding(5, 5, 5, 5)
                if(i==0) {
                    textView.gravity = Gravity.START
                    textView.setPadding(60, 5, 5,5 )
                } else {
                    textView.gravity = Gravity.CENTER
                }
                tableRow.addView(textView)
            }
//            var recId = UUID.randomUUID()
            binding.tbOrder.addView(tableRow, count)
            count++
        }
        binding.tvTotalHarga.text = "Rp13.400"
        Log.d("CONFIRM PAYMENT", "tabel selesai")
        recId = UUID.randomUUID()
        GlobalScope.launch {
            foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, 0, "Nasi",3000, 1))
            foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, 1, "Ayam Goreng",8000, 1))
            foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, 2, "Tahu",1000, 1))
            foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, 3, "Tempe",700, 2))
            foodDao.insertReceipt(Receipt(recId, 13400) )
            stored = foodDao.getAllReceiptFoodCrossRef()[0].receiptId
            Log.d("CONFIRM PAYMENT", "selesai insert")
        }

//        var recId = UUID.randomUUID()
//        for ((index, food) in listOrderedFood.withIndex()) {
//                GlobalScope.launch {

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

//        detector = FoodDetector(this)
//        detector.setup()
//        detector.detect(result)

//        val predictResult = mlTransform(result, this)


//        listOrderedFood = mlTransform(scannedBitmap, this)

//==========================

        binding.btnConfirmPayment.setOnClickListener {
            goToReceiptActivity()
        }
    }

    private fun addTextViewToTableRow(tableRow: TableRow, text: String) {
        val textView = TextView(this@ConfirmPaymentActivity)
        textView.text = text
        textView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        tableRow.addView(textView)
    }

    private fun goToReceiptActivity() {


        Log.d("CONFIRM PAYMENT", "Mau masuk receipt")
//        var recId = UUID.randomUUID()
//        GlobalScope.launch {
//            for(food in toStoreFoods) {
//                foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, food.foodId, food.name, food.price, food.quantity))
//            }
//            foodDao.insertReceipt(Receipt(recId, totalPrice))
//        }
        val intent = Intent(this, ReceiptActivity::class.java)
        Log.d("RECEIPT", stored.toString())
        intent.putExtra("ORDER_ID", recId.toString())

//        intent.putParcelableArrayListExtra("ordered_foods", ArrayList(toStoreFoods))
        startActivity(intent)
    }
}