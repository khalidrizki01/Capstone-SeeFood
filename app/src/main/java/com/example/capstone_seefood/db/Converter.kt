package com.example.capstone_seefood.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converter {
    @TypeConverter
    fun dateToLong(date: LocalDateTime) : Long {
        return date.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun longToDate(longDate: Long) : LocalDateTime {
        return LocalDateTime.ofEpochSecond(longDate, 0, ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray : ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}