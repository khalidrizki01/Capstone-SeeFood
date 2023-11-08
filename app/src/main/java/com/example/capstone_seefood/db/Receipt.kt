package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Parcelize
data class Receipt(
    @PrimaryKey //(autoGenerate = true)
    val receiptId : UUID,
    val totalPrice: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Parcelable {
    val totalTaxedPrice: Int = (totalPrice * 1.1).toInt()
}
