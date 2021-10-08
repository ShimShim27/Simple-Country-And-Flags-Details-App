package com.countries.flag.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.countries.flag.models.CountryJsonCache

@Dao
interface CountryJsonCacheDao {
    @Insert
    suspend fun saveCache(countryJsonCache: CountryJsonCache)

    @Query("SELECT * FROM country_json_cache")
    suspend fun getJsonCache(): CountryJsonCache?
}