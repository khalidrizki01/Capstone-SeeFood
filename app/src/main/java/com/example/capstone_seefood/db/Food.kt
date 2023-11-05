package com.example.capstone_seefood.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Food(
    @ColumnInfo(name="name") val name : String,
    @ColumnInfo(name="price") val price : Int? = null,
    @ColumnInfo(name="photo") val photo : Int,
    @ColumnInfo(name="is_sell") val isSell : Boolean = false
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
