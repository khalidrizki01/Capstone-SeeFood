package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_id") val receiptId : Long
) : Parcelable
