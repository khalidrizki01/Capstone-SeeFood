package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity
@Parcelize
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_id") val receiptId : Long,
    val createdAt: LocalDate = LocalDate.now(),
    val totalPrice: Int,
    val totalTaxedPrice: Int
) : Parcelable
