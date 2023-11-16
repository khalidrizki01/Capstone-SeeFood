package com.example.capstone_seefood

//import com.google.flatbuffers.Table

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_seefood.api.ApiConfig
import com.example.capstone_seefood.api.IdentifiedFood
import com.example.capstone_seefood.api.ResultModel
import com.example.capstone_seefood.databinding.ActivityConfirmPaymentBinding
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.TempFood
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.example.capstone_seefood.util.ImageScaler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        // Item yang akan menjadi object untuk dibawa ke activity selanjutnya
        toStoreFoods = mutableListOf()
        Log.d(TAG, "Berhasil ke confirm payment activity")
        super.onCreate(savedInstanceState)

        foodDao = FoodDatabase.getInstance(this).foodDao
        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Path gambar yang telah diambil
        val myFile = intent.getSerializableExtra(PICTURE) as File
        Log.d(TAG, myFile.path.toString())

        // Scale gambar
        val oriImage = BitmapFactory.decodeFile(myFile.path)
        val scaledImage = ImageScaler.scaleBitmap(oriImage)
        val scaledFile = ImageScaler.createImageFileFromBitmap(scaledImage)

        // Request body ke API machine learning
        val requestFile = scaledFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val requestFile = myFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            myFile.name,
            requestFile
        )
        recId = UUID.randomUUID()
        // Memanggil API
        val call = apiService.uploadImage(image)
        call.enqueue(object : Callback<ResultModel> {
            override fun onResponse(
                call: Call<ResultModel>,
                response: Response<ResultModel>
            ) {
                Log.d(TAG, "RESPONSE : ${response.body()}")
                if (response.isSuccessful) {
                    Log.d(TAG, "API berhasil")
                    val result = response.body()

                    // Jika berhasil, maka unpack kembalian dari API
                    listOrderedFood = result?.labels ?: emptyList()

                    // Iterasi tiap item dalam list ordered food
                    GlobalScope.launch {
                        for ((index, food) in listOrderedFood.withIndex()) {
                            // Mengambil data makanan dari database
                            var foodItem = foodDao.getFoodBasedOnName(food.label)
                            Log.d(TAG, "==========================================================")
                            Log.d(TAG, "Berhasil meng-query makanan dari database: ${foodItem.name}")

                            // Menginisialisasi table row kosong
                            val tableRow = TableRow(this@ConfirmPaymentActivity)
                            if (foodItem.isSell) {
                                // Menghitung total harga untuk masing-masing makanan (misal 2 ayam = 2 x Rp8.000)
                                val totalItemPrice = food.count * foodItem.price!!

                                // Menambahkan total item price ke total harga yang harus dibayar dalam 1 order
                                totalPrice += totalItemPrice
                                Log.d(TAG, "Total Price: ${totalPrice.toString()}")

                                // Menambahkan tiap data iterasi ke object parcel yang nanti dibawa ke activity selanjutnya
                                toStoreFoods.add(TempFood(foodItem.foodId, foodItem.name, food.count, foodItem.price!!, totalItemPrice))
                                Log.d(TAG, "hasil: ${toStoreFoods[index].foodId} | ${toStoreFoods[index].name} | ${toStoreFoods[index].price} | ${toStoreFoods[index].quantity} | ${toStoreFoods[index].totalItemPrice}")

                                // Menambahkan ke tabel row
                                addTextViewToTableRow(tableRow, foodItem.name)
                                Log.d(TAG, "Berhasil menambah name ${toStoreFoods[index].name} ke table row")
                                addTextViewToTableRow(tableRow, food.count.toString())
                                Log.d(TAG, "Berhasil menambah quantity ${toStoreFoods[index].quantity} ke table row")
                                addTextViewToTableRow(tableRow, foodItem.price.toString())
                                Log.d(TAG, "Berhasil menambah price ${toStoreFoods[index].price} ke table row")
                                addTextViewToTableRow(tableRow, totalItemPrice.toString())
                                Log.d(TAG, "Berhasil menambah total item price ${toStoreFoods[index].totalItemPrice} ke table row")
                                foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, foodItem.foodId, foodItem.name,foodItem.price, food.count))
                            }

                            GlobalScope.launch(Dispatchers.Main) {
                                binding.tbOrder.addView(tableRow, index+1)
                                Log.d(TAG, "Berhasil menambah table row ke table layout di xml")
                                if (index == listOrderedFood.size - 1) {
                                    // Jika ini adalah iterasi terakhir, maka tampilkan total harga ke table layout di xml
                                    binding.tvTotalHarga.text = totalPrice.toString()
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // Handle jika ada kesalahan pada permintaan
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
        val bitmap = BitmapFactory.decodeFile(myFile.path)
        binding.imgOrder.setImageBitmap(bitmap)

        binding.btnConfirmPayment.setOnClickListener {
            goToReceiptActivity()
        }
    }

    private fun addTextViewToTableRow(tableRow: TableRow, text: String) {
        val textView = TextView(this@ConfirmPaymentActivity)
        textView.text = text
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER // Menetapkan gravity ke tengah
        textView.layoutParams = params
        tableRow.addView(textView)
    }

    private fun goToReceiptActivity() {
        Log.d(TAG, "Mau masuk receipt")
//        var recId = UUID.randomUUID()
//        GlobalScope.launch {
//            for(food in toStoreFoods) {
//                foodDao.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(recId, food.foodId, food.name, food.price, food.quantity))
//            }
//            foodDao.insertReceipt(Receipt(recId, totalPrice))
//        }
        val intent = Intent(this, ReceiptActivity::class.java)
        Log.d(TAG, stored.toString())
        intent.putExtra("ORDER_ID", recId.toString())

//        intent.putParcelableArrayListExtra("ordered_foods", ArrayList(toStoreFoods))
        startActivity(intent)
    }
}