package com.example.capstone_seefood.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface FoodDao {
    @Query("SELECT * FROM food")
    fun getAllFoods():List<Food>

    @Query("SELECT * FROM Food WHERE is_sell = 1")
    fun getAvailableFood(): List<Food>

    @Query("SELECT * FROM Food WHERE is_sell = 0")
    fun getUnavailableFood(): List<Food>

    @Insert
    fun insert(vararg foods : Food)

    @Query("DELETE FROM book")
    fun deleteAll()
    @Update
    fun updateFood(food: Food)
}