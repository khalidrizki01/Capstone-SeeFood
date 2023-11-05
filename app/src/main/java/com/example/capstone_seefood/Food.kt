package com.example.capstone_seefood

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val name: String,
    val price: Int,
    val photo: Int,
    val isSell: Boolean = false
) : Parcelable
