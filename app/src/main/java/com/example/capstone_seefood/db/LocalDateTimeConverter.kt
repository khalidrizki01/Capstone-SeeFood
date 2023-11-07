package com.example.capstone_seefood.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeConverter {
    @TypeConverter
    fun dateToLong(date: LocalDateTime) : Long {
        return date.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun longToDate(longDate: Long) : LocalDateTime {
        return LocalDateTime.ofEpochSecond(longDate, 0, ZoneOffset.UTC)
    }
}