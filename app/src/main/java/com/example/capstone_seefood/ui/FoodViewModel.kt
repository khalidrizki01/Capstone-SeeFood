package com.example.capstone_seefood.ui

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.asLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.capstone_seefood.db.Food
//import com.example.capstone_seefood.db.Receipt
//import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef
//import com.example.capstone_seefood.db.repository.FoodRepository
//import kotlinx.coroutines.launch
//import java.time.LocalDateTime
//import java.util.Date
//import java.util.UUID
//
//class FoodViewModel (private val foodRepository: FoodRepository) : ViewModel() {
//    fun insertFood(foodName : String, photo : String, price : Int, isSell : Boolean = true){
//        viewModelScope.launch {
//            foodRepository.insertFood(Food(name = foodName, photo = photo, price = price, isSell = isSell))
//        }
//    }
//
//    fun insertReceipt(receiptId : UUID, totalPrice : Int){
//        viewModelScope.launch {
//            foodRepository.insertReceipt(Receipt(receiptId = receiptId, totalPrice = totalPrice))
//        }
//    }
//    fun insertReceiptFoodCrossRef(receiptId: UUID, foodId : Int, foodName: String, foodPrice : Int, quantity : Int) {
//        viewModelScope.launch {
//            foodRepository.insertReceiptFoodCrossRef(ReceiptFoodCrossRef(receiptId = receiptId, foodId = foodId, foodName = foodName, foodPrice = foodPrice, quantity = quantity))
//        }
//    }
//    fun getReceiptFrom(date: LocalDateTime) = foodRepository.getReceiptFrom(date).asLiveData(viewModelScope.coroutineContext)
//
//    fun getReceiptFoodCrossRefByReceiptId(recId: UUID) = foodRepository.getReceiptFoodCrossRefByReceiptId(recId).asLiveData(viewModelScope.coroutineContext)
//    fun getAllReceiptFoodCrossRef() = foodRepository.getAllReceiptFoodCrossRef().asLiveData(viewModelScope.coroutineContext)
//    fun getAvailableFood() = foodRepository.getAvailableFood().asLiveData(viewModelScope.coroutineContext)
//    fun getUnavailableFood() = foodRepository.getUnavailableFood().asLiveData(viewModelScope.coroutineContext)
//    fun getTotalItemPricePerFoodId(startDate : LocalDateTime) = foodRepository.getTotalItemPricePerFoodId(startDate).asLiveData(viewModelScope.coroutineContext)
//    fun getQuantityPerFoodId(startDate: LocalDateTime) = foodRepository.getQuantityPerFoodId(startDate).asLiveData(viewModelScope.coroutineContext)
//    fun getFoodBasedOnName(foodName : String) = foodRepository.getFoodBasedOnName(foodName).asLiveData(viewModelScope.coroutineContext)
//
//    fun deleteAllFood(){
//        viewModelScope.launch {
//            foodRepository.deleteAllFood()
//        }
//    }
//    fun deleteAllReceipts(){
//        viewModelScope.launch {
//            foodRepository.deleteAllReceipts()
//        }
//    }
//    fun deleteAllReceiptFoodCR() {
//        viewModelScope.launch {
//            foodRepository.deleteAllReceiptFoodCR()
//        }
//    }
//    fun updateFood(food: Food) {
//        viewModelScope.launch {
//            foodRepository.updateFood(food)
//        }
//    }
//}