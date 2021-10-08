package com.countries.flag.datasource.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.countries.flag.models.CountryJsonCache
import com.countries.flag.datasource.room.dao.CountryJsonCacheDao

@Database(entities = [CountryJsonCache::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun getCountryJsonCacheDao():CountryJsonCacheDao

    companion object {
        private var instance: MainDatabase? = null
        fun getInstance(context: Context): MainDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MainDatabase::class.java, "main_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}