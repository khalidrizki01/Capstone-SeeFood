package com.example.capstone_seefood.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.capstone_seefood.db.relations.FoodWithReceipts
//import com.example.capstone_seefood.db.relations.FoodWithReceipts
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.example.capstone_seefood.db.relations.ReceiptWithFoods
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface FoodDao {
//  Untuk meng-insert data makanan
    @Insert
    suspend fun insertFood(food : Food)

//  Untuk meng-insert nota
    @Insert
    suspend fun insertReceipt(receipt : Receipt)

//  Untuk meng-insert hubungan antara nota dengan makanan
    @Insert
    suspend fun insertReceiptFoodCrossRef(crossRef: ReceiptFoodCrossRef)

//    Insert Many Receipt Food Cross Ref: Ketika menyimpan data receipt?

//  Untuk menngambil data makanan yang dijual
    @Query("SELECT * FROM food WHERE issell = 1")
    fun getAvailableFood(): List<Food>

//  Untuk mengambil data makanan yang tidak dijual
    @Query("SELECT * FROM food WHERE issell = 0")
    fun getUnavailableFood(): List<Food>

    @Query("SELECT * FROM receipt WHERE createdAt >= :date")
    suspend fun getReceiptFrom(date: LocalDateTime) : List<Receipt>

//  Untuk mengambil data makanan yang terjual dari satu nota/penjualan
    @Transaction
    @Query("SELECT * FROM receipt WHERE receiptid = :receiptId")
    suspend fun getReceiptWithFoods(receiptId : UUID) : List<ReceiptWithFoods>

//  Untuk mengambil data penjualan dari satu makanan
    @Transaction
    @Query("SELECT * FROM food WHERE foodid = :foodId")
    suspend fun getFoodWithReceipts(foodId : Long) : List<FoodWithReceipts>

    @Query("DELETE FROM food")
    fun deleteAllFood()
    @Update
    fun updateFood(food: Food)


}