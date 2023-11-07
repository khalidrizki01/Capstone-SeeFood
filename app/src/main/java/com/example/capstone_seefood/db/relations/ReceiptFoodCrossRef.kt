package com.example.capstone_seefood.db.relations

import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["receiptId", "foodId"])
data class ReceiptFoodCrossRef(
    val receiptId: UUID,
    val foodId: UUID,
    val quantity: Int,
    val totalItemPrice: Int
)
