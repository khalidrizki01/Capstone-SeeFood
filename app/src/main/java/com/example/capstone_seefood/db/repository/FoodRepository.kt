package com.example.capstone_seefood.db.repository

import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.FoodDao
import com.example.capstone_seefood.db.FoodDatabase
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import java.time.LocalDateTime

//
class FoodRepository (private val foodDatabase : FoodDatabase) {
    suspend fun insertFood(food: Food){
        foodDatabase.foodDao.insertFood(food)
    }
    suspend fun insertReceipt(receipt: Receipt) {
        foodDatabase.foodDao.insertReceipt(receipt)
    }
    suspend fun insertReceiptFoodCrossRef(crossRef: ReceiptFoodCrossRef) {
        foodDatabase.foodDao.insertReceiptFoodCrossRef(crossRef)
    }
    fun getAllReceiptFoodCrossRef() = foodDatabase.foodDao.getAllReceiptFoodCrossRef()
    fun getAvailableFood() = foodDatabase.foodDao.getAvailableFood()
    fun geUnavailableFood() =  foodDatabase.foodDao.getUnavailableFood()
    fun getTotalItemPricePerFoodId(startDate: LocalDateTime){
        foodDatabase.foodDao.getTotalItemPricePerFoodId(startDate)
    }
    fun getQuantityPerFoodId(startDate: LocalDateTime) {
        foodDatabase.foodDao.getQuantityPerFoodId(startDate)
    }
    suspend fun getFoodBasedOnName(foodName : String) {
        foodDatabase.foodDao.getFoodBasedOnName(foodName)
    }
    fun getReceiptFrom(date : LocalDateTime) = foodDatabase.foodDao.getReceiptFrom(date)
    suspend fun deleteAllFood() {
        foodDatabase.foodDao.deleteAllFood()
    }
    suspend fun deleteAllReceipts() {
        foodDatabase.foodDao.deleteAllReceipts()
    }
    suspend fun deleteAllReceiptFoodCR(){
        foodDatabase.foodDao.deleteAllReceiptFoodCR()
    }
    suspend fun UpdateFood(food: Food){
        foodDatabase.foodDao.updateFood(food)
    }
}