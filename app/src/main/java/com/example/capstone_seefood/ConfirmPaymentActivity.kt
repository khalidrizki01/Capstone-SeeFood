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
    private lateinit var toStoreFoods : MutableList<ReceiptFoodCrossRef>
    private val apiService = ApiConfig.getApiService()
    private lateinit var recId : UUID
    private var totalPrice = 0

    companion object {
        const val PICTURE = "picture"
        private const val TAG = "ConfirmPaymentActivity"
    }

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
                                // Menambahkan tiap data iterasi ke object parcel yang nanti dibawa ke activity selanjutnya
                                var crossRef = ReceiptFoodCrossRef(recId, foodItem.foodId, foodItem.name,foodItem.price!!,  food.count)
                                toStoreFoods.add(crossRef)
//                                Log.d(TAG, "hasil: ${toStoreFoods[index].foodId} | ${toStoreFoods[index].name} | ${toStoreFoods[index].price} | ${toStoreFoods[index].quantity} | ${toStoreFoods[index].totalItemPrice}")

                                // Menambahkan ke tabel row
                                addTextViewToTableRow(tableRow, foodItem.name)
                                addTextViewToTableRow(tableRow, food.count.toString())
                                addTextViewToTableRow(tableRow, "Rp${foodItem.price.toString()}")
                                addTextViewToTableRow(tableRow, "Rp${crossRef.totalItemPrice}")
                                Log.d(TAG, crossRef.totalItemPrice.toString())

                                totalPrice += crossRef.totalItemPrice!!
                            }

                            GlobalScope.launch(Dispatchers.Main) {
                                binding.tbOrder.addView(tableRow, index+1)
                                Log.d(TAG, "Berhasil menambah table row ke table layout di xml")
                                if (index == listOrderedFood.size - 1) {
                                    // Jika ini adalah iterasi terakhir, maka tampilkan total harga ke table layout di xml
                                    binding.tvTotalHarga.text = "Rp$totalPrice"
                                    Log.d(TAG, "toStoreFoods: ${toStoreFoods}")
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
        Log.d(TAG, "Berhasil menambah name $text ke table row")
    }

    private fun goToReceiptActivity() {
        Log.d(TAG, "Mau masuk receipt")
        addReceiptToDatabase()
        val intent = Intent(this, ReceiptActivity::class.java)
        Log.d(TAG, recId.toString())
        intent.putExtra("confirmedReceiptId", recId)
        startActivity(intent)
    }

    private fun addReceiptToDatabase(){
        GlobalScope.launch {
            foodDao.insertMultipleReceiptFoodCrossRef(toStoreFoods)
            foodDao.insertReceipt(Receipt(recId, totalPrice))
            Log.d(TAG, "Berhasil menyimpan ke database")
        }
    }
}