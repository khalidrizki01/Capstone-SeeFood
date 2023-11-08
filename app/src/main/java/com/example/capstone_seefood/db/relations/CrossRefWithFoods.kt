package com.example.capstone_seefood.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.capstone_seefood.db.Food

data class CrossRefWithFoods (
    @Embedded
    val receiptFood: ReceiptFoodCrossRef,
    @Relation(entity = Food::class, parentColumn = "foodId", entityColumn = "foodId")
    val foodList : List<Food>
)