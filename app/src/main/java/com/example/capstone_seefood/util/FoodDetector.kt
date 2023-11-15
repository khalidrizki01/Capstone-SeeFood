package com.example.capstone_seefood.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.metadata.schema.BoundingBoxType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.min

class FoodDetector(private val context: Context) {
    private var interpreter: Interpreter? = null

    private val imageProcessor = ImageProcessor.Builder()
//        .add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
        .add(CastOp(INPUT_IMAGE_TYPE))
        .build()

    fun setup() {
        val model = FileUtil.loadMappedFile(context, "best.tflite")
        val options = Interpreter.Options()
        options.numThreads = 4
        interpreter = Interpreter(model, options)
    }

    fun clear() {
        interpreter?.close()
        interpreter = null
    }

    private var squareSize = 0
    private var left = 0
    private var top = 0

    fun detect(frame: Bitmap)  {
        interpreter ?: return
        if(squareSize == 0){
            squareSize = min(frame.width, frame.height)
            left = (frame.width - squareSize) / 2
            top = (frame.height - squareSize) / 2
        }
        val croppedBitmap = Bitmap.createBitmap(frame, left, top, squareSize, squareSize)
        val resizedBitmap = Bitmap.createScaledBitmap(croppedBitmap, TENSOR_WIDTH, TENSOR_HEIGHT, false)

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)

        val processedImage = imageProcessor.process(tensorImage)
        val imageBuffer = processedImage.buffer

        val output = TensorBuffer.createFixedSize(intArrayOf(1 , 9, NUM_ELEMENTS), OUTPUT_IMAGE_TYPE)

//        Log.d("INPUT TO INTERPRETER", imageBuffer.toString())
//        Log.d("COMP of INPUT", imageBuffer[0].toString())

        interpreter?.run(imageBuffer, output.buffer)
        Log.d("OUTPUT", output.toString())
        Log.d("OUTPUT datatype", output.dataType.toString())
        Log.d("OUTPUT first comp datatype", output.shape.toString())
        val bestBoxes = bestBox(output.floatArray)
        if(bestBoxes!= null){
            for(box in bestBoxes){
                Log.d("BOUNDING BOX", "bounding\n: cx:${box.cx}, cy:${box.cy}, w:${box.w}, h:${box.h}")
            }

        }


    }

    private fun bestBox(array: FloatArray) : List<BoundingBox>? {

        val boundingBoxes = mutableListOf<BoundingBox>()
        for (c in 0 until NUM_ELEMENTS) {
            val cnf = array[c + NUM_ELEMENTS * 4]
            if (cnf > CONFIDENCE_THRESHOLD) {
                val cx = array[c]
                val cy = array[c + NUM_ELEMENTS]
                val w = array[c + NUM_ELEMENTS * 2]
                val h = array[c + NUM_ELEMENTS * 3]
                val x1 = cx - (w/2F)
                val y1 = cy - (h/2F)
                val x2 = cx + (w/2F)
                val y2 = cy + (h/2F)
                if (x1 <= 0F || x1 >= TENSOR_WIDTH_FLOAT) continue
                if (y1 <= 0F || y1 >= TENSOR_HEIGHT_FLOAT) continue
                if (x2 <= 0F || x2 >= TENSOR_WIDTH_FLOAT) continue
                if (y2 <= 0F || y2 >= TENSOR_HEIGHT_FLOAT) continue
                boundingBoxes.add(
                    BoundingBox(
                        x1 = x1, y1 = y1, x2 = x2, y2 = y2,
                        cx = cx, cy = cy, w = w, h = h, cnf = cnf
                    )
                )
            }
        }

        if (boundingBoxes.isEmpty()) return null

        return applyNMS(boundingBoxes)
    }

    private fun applyNMS(boxes: List<BoundingBox>) : MutableList<BoundingBox> {
        val sortedBoxes = boxes.sortedByDescending { it.w * it.h }.toMutableList()
        val selectedBoxes = mutableListOf<BoundingBox>()

        while(sortedBoxes.isNotEmpty()) {
            val first = sortedBoxes.first()
            selectedBoxes.add(first)
            sortedBoxes.remove(first)

            val iterator = sortedBoxes.iterator()
            while (iterator.hasNext()) {
                val nextBox = iterator.next()
                val iou = calculateIoU(first, nextBox)
                if (iou >= IOU_THRESHOLD) {
                    iterator.remove()
                }
            }
        }

        return selectedBoxes
    }

    private fun calculateIoU(box1: BoundingBox, box2: BoundingBox): Float {
        val x1 = maxOf(box1.x1, box2.x1)
        val y1 = maxOf(box1.y1, box2.y1)
        val x2 = minOf(box1.x2, box2.x2)
        val y2 = minOf(box1.y2, box2.y2)
        val intersectionArea = maxOf(0F, x2 - x1) * maxOf(0F, y2 - y1)
        val box1Area = box1.w * box1.h
        val box2Area = box2.w * box2.h
        return intersectionArea / (box1Area + box2Area - intersectionArea)
    }

    companion object {
        private const val TENSOR_WIDTH = 640
        private const val TENSOR_HEIGHT = 640
        private const val TENSOR_WIDTH_FLOAT = TENSOR_WIDTH.toFloat()
        private const val TENSOR_HEIGHT_FLOAT = TENSOR_HEIGHT.toFloat()

        private const val INPUT_MEAN = 0f
        private const val INPUT_STANDARD_DEVIATION = 255f

        private val INPUT_IMAGE_TYPE = DataType.FLOAT32
        private val OUTPUT_IMAGE_TYPE = DataType.FLOAT32

        private const val NUM_ELEMENTS = 8400
        private const val CONFIDENCE_THRESHOLD = 0.75F
        private const val IOU_THRESHOLD = 0.5F
    }
}