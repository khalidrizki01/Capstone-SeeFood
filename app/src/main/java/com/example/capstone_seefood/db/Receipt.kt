package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.UUID

@Entity
@Parcelize
data class Receipt(
    @PrimaryKey //(autoGenerate = true)
    val receiptId : UUID,
//    val createdAt: LocalDate = LocalDate.now(),
    val totalPrice: Int,
    val totalTaxedPrice: Int
) : Parcelable
