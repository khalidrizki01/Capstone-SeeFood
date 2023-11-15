package com.example.capstone_seefood.util

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

object ImageScaler {
    private var squareSize = 0
    private var left = 0
    private var top = 0
    private val WIDTH = 640
    private val HEIGHT = 640
    fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (squareSize == 0) {
            squareSize = min(bitmap.width, bitmap.height)
            left = (bitmap.width - squareSize) / 2
            top = (bitmap.height - squareSize) / 2
        }
        val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, squareSize, squareSize)

        return Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true)
    }

    fun createImageFileFromBitmap(bitmap: Bitmap): File {
        val file = createTempFile("scaled_image", ".jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return file
    }
}