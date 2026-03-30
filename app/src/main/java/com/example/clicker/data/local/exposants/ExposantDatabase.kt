package com.example.clicker.data.local.exposants

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ExposantEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExposantConverters::class)
abstract class ExposantDatabase : RoomDatabase() {

    abstract fun exposantDao(): ExposantDao

    companion object {
        @Volatile
        private var Instance: ExposantDatabase? = null

        fun getDatabase(context: Context): ExposantDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = ExposantDatabase::class.java,
                    name = "exposant_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}