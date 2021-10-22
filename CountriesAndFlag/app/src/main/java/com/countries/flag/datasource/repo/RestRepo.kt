package com.countries.flag.datasource.repo

import android.util.Log
import com.countries.flag.datasource.retrofit.CountryLayerRestApi
import com.countries.flag.datasource.retrofit.GithubRestApi
import com.countries.flag.datasource.room.database.MainDatabase
import com.countries.flag.models.Country
import com.countries.flag.models.CountryJsonCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class RestRepo(
    mainDatabase: MainDatabase,
    private val countryLayerApi: CountryLayerRestApi,
    private val rawGithubApi: GithubRestApi,
    private val gistGithubApi: GithubRestApi
) {
    private val countryJsonCacheDao = mainDatabase.getCountryJsonCacheDao()


    suspend fun getCountries(): List<Country> {
        val countryJsonCache = countryJsonCacheDao.getJsonCache()
        val gson = Gson()

        return if (countryJsonCache == null) {
            val countriesFromCountryLayerApi = getCountriesFromCountryLayer()
            val withCurrencyBorderLanguage = getCountriesWithCurrencyBorderLanguage()
            val withPopulation = getCountriesWithPopulation()

            withCurrencyBorderLanguage.forEach { cCurrencyBorderLanguage ->
                countriesFromCountryLayerApi.filter { country -> country.alpha2Code == cCurrencyBorderLanguage.alpha2 }
                    .forEach { filtered ->
                        val latlng = cCurrencyBorderLanguage.latlng
                        filtered.latitude = latlng[0]
                        filtered.longitude = latlng[1]
                        filtered.languages = cCurrencyBorderLanguage.languages.values.toList()
                        filtered.borders = cCurrencyBorderLanguage.borders
                        filtered.currencies =
                            cCurrencyBorderLanguage.currencies.values.map { it.name }
                    }
            }


            withPopulation.forEach { cPopulation ->
                countriesFromCountryLayerApi.filter { country -> country.alpha2Code == cPopulation.alpha2 }
                    .forEach { filtered ->
                        filtered.population = cPopulation.population
                    }
            }

            countryJsonCacheDao.saveCache(
                CountryJsonCache(
                    0,
                    gson.toJson(countriesFromCountryLayerApi)
                )
            )


            countriesFromCountryLayerApi
        } else {
            val type = object : TypeToken<List<Country>>() {}.type
            gson.fromJson(countryJsonCache.jsonCache, type)
        }

    }


    private fun getCountriesFromCountryLayer(): List<Country> {
        return countryLayerApi.getCountries().execute().body()!!
    }


    private fun getCountriesWithCurrencyBorderLanguage(): List<GithubRestApi.CountryWithCurrencyBorderLanguage> {
        return rawGithubApi.getCountriesWithCurrencyBorderLanguage().execute().body()!!
    }

    private fun getCountriesWithPopulation(): List<GithubRestApi.CountryWithPopulationResponse.CountryWithPopulation> {
        return gistGithubApi.getCountriesWithPopulation().execute()
            .body()!!.countries.country
    }


}