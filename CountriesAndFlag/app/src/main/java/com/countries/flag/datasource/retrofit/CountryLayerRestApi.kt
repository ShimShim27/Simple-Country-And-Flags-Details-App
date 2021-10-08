package com.countries.flag.datasource.retrofit

import com.countries.flag.config.APICONFIG
import com.countries.flag.models.Country
import retrofit2.Call
import retrofit2.http.GET


interface CountryLayerRestApi {
    @GET(APICONFIG.COUNTRY_LAYER_RELATIVE_URL)
    fun getCountries(): Call<List<Country>>
}