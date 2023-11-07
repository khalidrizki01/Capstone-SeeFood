package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity
@Parcelize
data class Food(
    @PrimaryKey //(autoGenerate = true)
    val foodId : UUID,
    val name : String,
    val price : Int? = null,
    val photo : Int,
    val isSell : Boolean = false
) : Parcelable
