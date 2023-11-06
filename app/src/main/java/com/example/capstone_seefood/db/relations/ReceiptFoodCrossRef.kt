package com.example.capstone_seefood.db.relations

import androidx.room.Entity

@Entity(primaryKeys = ["receiptId", "foodId"])
data class ReceiptFoodCrossRef(
    val receiptId: Long,
    val foodId: Long,
    val quantity: Int
)
