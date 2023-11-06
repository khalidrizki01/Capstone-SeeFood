package com.example.capstone_seefood.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.Receipt

data class ReceiptWithFoods(
    @Embedded val receipt : Receipt,
    @Relation(
        parentColumn = "receiptId",
        entityColumn = "foodId",
        associateBy = Junction(ReceiptFoodCrossRef::class)
    )
    val foods : List<Food>
)
