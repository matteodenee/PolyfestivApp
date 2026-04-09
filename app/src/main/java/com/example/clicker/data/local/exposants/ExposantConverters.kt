package com.example.clicker.data.local.exposants

import androidx.room.TypeConverter

class ExposantConverters {

    @TypeConverter
    fun fromTypeList(value: List<String>): String {
        return value.joinToString(separator = "|")
    }

    @TypeConverter
    fun toTypeList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split("|").map { it.trim() }.filter { it.isNotEmpty() }
    }
}