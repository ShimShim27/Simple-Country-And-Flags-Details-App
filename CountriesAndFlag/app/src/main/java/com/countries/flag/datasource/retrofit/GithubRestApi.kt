package com.countries.flag.datasource.retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface GithubRestApi {
    @GET("mledoze/countries/master/countries.json")
    fun getCountriesWithCurrencyBorderLanguage(): Call<List<CountryWithCurrencyBorderLanguage>>

    @GET("tiagodealmeida/0b97ccf117252d742dddf098bc6cc58a/raw/f621703926fc13be4f618fb4a058d0454177cceb/countries.json")
    fun getCountriesWithPopulation(): Call<CountryWithPopulationResponse>

    data class CountryWithCurrencyBorderLanguage(
        @SerializedName("latlng")
        val latlng: List<Double>,
        @SerializedName("cca2")
        val alpha2: String,
        @SerializedName("languages")
        val languages: Map<String, String>,
        @SerializedName("currencies")
        val currencies: Map<String, CurrencyName>,
        @SerializedName("borders")
        val borders: List<String>
    ) {
        data class CurrencyName(
            @SerializedName("name")
            val name: String
        )
    }


    data class CountryWithPopulationResponse(
        @SerializedName("countries")
        val countries: Map<String, List<CountryWithPopulation>>
    ) {
        data class CountryWithPopulation(
            @SerializedName("countryCode")
            val alpha2: String,
            @SerializedName("population")
            val population:Long
        )
    }

}