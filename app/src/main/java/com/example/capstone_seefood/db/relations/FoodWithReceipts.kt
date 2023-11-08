package com.example.capstone_seefood.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.Receipt


data class FoodWithReceipts(
    @Embedded val food : Food,
    @Relation(
        parentColumn = "foodId",
        entityColumn = "receiptId",
        associateBy = Junction(ReceiptFoodCrossRef::class)
    )
    val receipts : List<Receipt>
)