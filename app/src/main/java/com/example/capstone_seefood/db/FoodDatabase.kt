package com.example.capstone_seefood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.capstone_seefood.db.relations.ReceiptFoodCrossRef

@Database(
    entities = [
        Food::class,
        Receipt::class,
        ReceiptFoodCrossRef::class
    ],
    version=2
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class FoodDatabase : RoomDatabase() {
    abstract val foodDao : FoodDao

    companion object {
        @Volatile
        private var INSTANCE : FoodDatabase? = null

        fun getInstance(context: Context) : FoodDatabase{
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food-db"
                ).fallbackToDestructiveMigration().build().also{
                    INSTANCE = it
                }
            }
        }
    }
}