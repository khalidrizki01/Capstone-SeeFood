package com.example.capstone_seefood.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempFood(
    //(autoGenerate = true)
    val foodId : Int,
    val name : String,
    val quantity : Int,
    val price : Int,
    val totalItemPrice : Int
) : Parcelable
