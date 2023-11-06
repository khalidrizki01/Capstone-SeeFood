package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Food(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id") val foodId : Long,
    val name : String,
    val price : Int? = null,
    val photo : Int,
    @ColumnInfo(name = "is_sell")val isSell : Boolean = false
) : Parcelable
