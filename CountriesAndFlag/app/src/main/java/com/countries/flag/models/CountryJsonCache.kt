package com.countries.flag.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_json_cache")
data class CountryJsonCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val jsonCache: String
)