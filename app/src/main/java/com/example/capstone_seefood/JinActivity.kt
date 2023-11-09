package com.example.capstone_seefood
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.capstone_seefood.databinding.ActivityJinBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_seefood.ml.BestFloat32
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class JinActivity :AppCompatActivity(){

    lateinit var btnload : Button
    lateinit var btnprediksi : Button
    lateinit var tvgambar : ImageView
    lateinit var textpredict : TextView
    lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jin)

        btnload = findViewById(R.id.btnload)
        btnprediksi = findViewById(R.id.btnprediksi)
                tvgambar = findViewById(R.id.tvgambar)
                textpredict = findViewById(R.id.textpredict)

        var labels = application.assets.open("prediction.txt").bufferedReader().readLines()
        var imageProcessor = ImageProcessor.Builder()
            .add(NormalizeOp(0.0f,255.0f))

            .add(ResizeOp(640,640,ResizeOp.ResizeMethod.BILINEAR))
            .build()
        btnload.setOnClickListener {
            var intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,100)
        }

        btnprediksi.setOnClickListener {
            var tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)
            val model = BestFloat32.newInstance(this)

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 640, 640, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var maxIdx=0
            outputFeature0.forEachIndexed{ index,fl->
                if(outputFeature0[maxIdx]<fl){
                    maxIdx=index
                }
            }
            textpredict.text = labels[maxIdx]
// Releases model resources if no longer used.
            model.close()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100){
            var uri=data?.data;
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            tvgambar.setImageBitmap(bitmap)


        }
    }
}