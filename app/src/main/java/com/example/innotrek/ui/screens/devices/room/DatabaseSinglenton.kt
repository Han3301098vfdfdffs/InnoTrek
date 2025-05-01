package com.example.innotrek.ui.screens.devices.room

import android.content.Context
import androidx.room.Room

object DatabaseSingleton {
    private var _database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return _database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "bluetooth-db"
            )
                .fallbackToDestructiveMigration() // Para desarrollo, en producción usa migraciones propias
                .build()
            _database = instance
            instance
        }
    }
}