package com.example.clicker.data.local.exposant

import androidx.room.TypeConverter

class ExposantConverters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(separator = "|")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split("|")
    }
}