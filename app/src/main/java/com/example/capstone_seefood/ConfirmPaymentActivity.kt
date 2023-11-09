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
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import com.example.capstone_seefood.ml.BestFloat32
import com.google.flatbuffers.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private lateinit var foods : List<Food>
    private lateinit var identifiedFoods : List<String>
    private lateinit var foodQuantities : List<Int>
    private lateinit var foodDao : FoodDao
    private lateinit var scannedBitmap : Bitmap
    private lateinit var listOrderedFood : List<IdentifiedFoodAndConf>

    companion object {
        const val SCANNED_IMAGE_BYTES = "scannedImageBytes"
    }


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

        val scannedImageBytes = intent.getByteArrayExtra("scannedImageBytes")
        Log.d("IMAGE BYTES", scannedImageBytes.toString())
        if (scannedImageBytes != null) {
            val scannedBitmap = BitmapFactory.decodeByteArray(scannedImageBytes, 0, scannedImageBytes.size)
            Log.d("BITMAP", scannedBitmap.toString())
            // Gunakan scannedBitmap sesuai kebutuhan di ConfirmPaymentActivity
        }

        listOrderedFood = mlTransform(scannedBitmap, this)
        for ((name, conf) in listOrderedFood) {
            val tableRow = TableRow(this)
            val textView = TextView(this)
            textView.text = name
            textView.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.addView(textView)
            binding.tbOrder.addView(tableRow)
        }


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

    fun mlTransform(bitmap: Bitmap, context: Context): List<IdentifiedFoodAndConf> {
        val filename = "class.txt"

        val inputString = context.resources.assets.open(filename).bufferedReader().use { it.readText() }
        val foodList = inputString.split("\n")

//    val model = Model.newInstance(context)
        val model = BestFloat32.newInstance(context)

        var resized = Bitmap.createScaledBitmap(bitmap, 640, 640, true)

        // Initialize a TensorImage
        val tensorImage = TensorImage(DataType.FLOAT32)

        // Load Bitmap into TensorImage
        tensorImage.load(resized)

        // Create an ImageProcessor
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f)) // Normalize pixel values to [0,1]
            .build()

        // Process the TensorImage
        val processedImage = imageProcessor.process(tensorImage)

        Log.d("hasilinputFeature0", processedImage.toString())

        val outputs = model.process(processedImage.tensorBuffer)

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        val outputList = outputFeature0.toList()
        Log.d("hasilcek", "$outputList")

        var identified = getIdentifiedFoods(foodList, outputFeature0)

        identified.forEach{item -> Log.d("teridentifikasi", "${item.foodName} : ${item.confidence}")}
        // binding.resultPred.text = townList[max]

        model.close()

        return identified
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