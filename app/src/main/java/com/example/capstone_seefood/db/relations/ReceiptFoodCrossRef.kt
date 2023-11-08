package com.example.capstone_seefood.db.relations

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(primaryKeys = ["receiptId", "foodId"])
data class ReceiptFoodCrossRef(
    val receiptId: UUID,
    val foodId: UUID,
    val quantity: Int,
) : Parcelable {
    var totalItemPrice : Int? = null
    fun calculateTotalItemPrice(foodPrice:Int) : Int{
        var totalItemPrice =  this.quantity * foodPrice
        this.totalItemPrice = totalItemPrice
        return totalItemPrice
    }
}
