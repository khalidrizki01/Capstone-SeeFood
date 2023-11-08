package com.example.capstone_seefood.db

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(indices = [Index(value = ["name"],
    unique = true)])
@Parcelize
data class Food(
    @PrimaryKey //(autoGenerate = true)
    val foodId : UUID,
    val name : String,
    val photo : Bitmap,
    val price : Int? = null,
    val isSell : Boolean = false
) : Parcelable
