package com.example.capstone_seefood.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.capstone_seefood.db.relations.FoodSum
import com.example.capstone_seefood.db.relations.FoodWithReceipts
//import com.example.capstone_seefood.db.relations.FoodWithReceipts
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.example.capstone_seefood.db.relations.ReceiptWithFoods
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface FoodDao {
//    fun getAllFullReceipts() : List<ReceiptWithFoods>
    //  Untuk mengambil data makanan yang terjual dari satu nota/penjualan
//    @Transaction
//    @Query("SELECT * FROM receipt WHERE receiptid = :receiptId")
//    fun getReceiptWithFoods(receiptId : UUID) : List<ReceiptWithFoods>

//  Untuk mengambil data penjualan dari satu makanan
//    @Transaction
//    @Query("SELECT * FROM food WHERE foodid = :foodId")
//    fun getFoodWithReceipts(foodId : Long) : List<FoodWithReceipts>

//    @Transaction
//    @Query("SELECT * FROM receipt WHERE receiptid == :recid LIMIT 1")
//    fun getReceiptWithFoodsById(recid : String) : Flow<List<ReceiptWithFoods>>

//  Untuk meng-insert data makanan
    @Insert
    suspend fun insertFood(food : Food)

//  Untuk meng-insert nota
    @Insert
    suspend fun insertReceipt(receipt : Receipt)

//  Untuk meng-insert hubungan antara nota dengan makanan
    @Insert
    fun insertReceiptFoodCrossRef(crossRef: ReceiptFoodCrossRef)

    @Query("SELECT * FROM receiptfoodcrossref WHERE receiptId == :recId")
//    fun getReceiptFoodCrossRefByReceiptId(recId : UUID) : Flow<List<ReceiptFoodCrossRef>>
    suspend fun getReceiptFoodCrossRefByReceiptId(recId : UUID) : List<ReceiptFoodCrossRef>

    @Query("SELECT * FROM receiptfoodcrossref")
//    fun getAllReceiptFoodCrossRef() : Flow<List<ReceiptFoodCrossRef>>
    fun getAllReceiptFoodCrossRef() : List<ReceiptFoodCrossRef>

//    Insert Many Receipt Food Cross Ref: Ketika menyimpan data receipt?

//  Untuk mengambil data makanan yang dijual
    @Query("SELECT * FROM food WHERE issell == 1")
//    fun getAvailableFood(): Flow<List<Food>>
    fun getAvailableFood(): List<Food>

//  Untuk mengambil data makanan yang tidak dijual
    @Query("SELECT * FROM food WHERE issell == 0")
//    fun getUnavailableFood(): Flow<List<Food>>
    suspend fun getUnavailableFood(): List<Food>

    @Transaction
//    @Query("SELECT foodId, SUM(totalItemPrice) as sum FROM ReceiptFoodCrossRef WHERE receiptId IN (SELECT receiptId FROM Receipt WHERE createdAt > :startDate) GROUP BY foodId")
    @Query("""
        SELECT f.name, SUM(rf.totalItemPrice) as sum
        FROM ReceiptFoodCrossRef rf
        INNER JOIN Food f ON rf.foodId = f.foodId
        WHERE rf.receiptId IN (SELECT receiptId FROM Receipt WHERE createdAt >= :startDate)
        GROUP BY rf.foodId
    """)
//    fun getTotalItemPricePerFoodId(startDate: LocalDateTime): Flow<List<FoodSum>>
    suspend fun getTotalItemPricePerFoodId(startDate: LocalDateTime): List<FoodSum>

    @Transaction
//    @Query("SELECT foodId, SUM(quantity) as sum FROM ReceiptFoodCrossRef WHERE receiptId IN (SELECT receiptId FROM Receipt WHERE createdAt > :startDate) GROUP BY foodId")
    @Query("""
        SELECT rf.foodId, f.name, SUM(rf.quantity) as sum
        FROM ReceiptFoodCrossRef rf
        INNER JOIN Food f ON rf.foodId = f.foodId
        WHERE rf.receiptId IN (SELECT receiptId FROM Receipt WHERE createdAt >= :startDate)
        GROUP BY rf.foodId
    """)
//    fun getQuantityPerFoodId(startDate: LocalDateTime): Flow<List<FoodSum>>
    suspend fun getQuantityPerFoodId(startDate: LocalDateTime): List<FoodSum>

    @Transaction
    @Query("SELECT * FROM food WHERE name == :foodName LIMIT 1")
//    fun getFoodBasedOnName(foodName : String) : Flow<Food>
    suspend fun getFoodBasedOnName(foodName : String) : Food

    @Query("SELECT * FROM receipt WHERE createdAt >= :date")
//    fun getReceiptFrom(date: LocalDateTime) : Flow<List<Receipt>>
    suspend fun getReceiptFrom(date: LocalDateTime) : List<Receipt>

    @Query("DELETE FROM food")
    suspend fun deleteAllFood()

    @Query("DELETE FROM receipt")
    suspend fun deleteAllReceipts()

    @Query("DELETE FROM receiptfoodcrossref")
    suspend fun deleteAllReceiptFoodCR()
    @Update
    suspend fun updateFood(food: Food)
}