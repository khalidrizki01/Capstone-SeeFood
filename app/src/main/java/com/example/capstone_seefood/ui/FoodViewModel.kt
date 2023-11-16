package com.example.capstone_seefood.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone_seefood.db.Food
import com.example.capstone_seefood.db.Receipt
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
import com.example.capstone_seefood.db.repository.FoodRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel (private val foodRepository: FoodRepository) : ViewModel() {
    fun insertFood(foodName : String, photo : String, price : Int, isSell){
        viewModelScope.launch {
            foodRepository.insertFood(Food(name = foodName, photo = photo, price = price, isSell = isSell))
        }
    }


}