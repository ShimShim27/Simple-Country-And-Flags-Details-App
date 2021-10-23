package com.countries.flag.datasource.retrofit

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import retrofit2.Call
import retrofit2.http.GET
import java.lang.Exception

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
        val currencies: Map<String, Currencies>,
        @SerializedName("borders")
        val borders: List<String>
    ) {
        data class Currencies(
            @SerializedName("name")
            val name: String
        )
    }


    data class CountryWithPopulationResponse(
        @SerializedName("countries")
        val countries: CountryList
    ) {

        data class CountryList(
            @SerializedName("country")
            val country: List<CountryWithPopulation>
        )


        data class CountryWithPopulation(
            @SerializedName("countryCode")
            val alpha2: String,
            @SerializedName("population")
            val population: Long
        )
    }


    class CountryWithCurrencyBorderLanguageListTypeAdapter :
        TypeAdapter<List<CountryWithCurrencyBorderLanguage>>() {
        override fun write(out: JsonWriter?, value: List<CountryWithCurrencyBorderLanguage>?) {

        }

        override fun read(reader: JsonReader?): List<CountryWithCurrencyBorderLanguage> {
            val countries = mutableListOf<CountryWithCurrencyBorderLanguage>()

            if (reader != null) {
                reader.beginArray()
                while (reader.hasNext()) {
                    reader.beginObject()

                    val latlng = mutableListOf<Double>()
                    var alpha2 = ""
                    val languages = mutableMapOf<String, String>()
                    val currencies =
                        mutableMapOf<String, CountryWithCurrencyBorderLanguage.Currencies>()
                    val borders = mutableListOf<String>()

                    while (reader.hasNext()) {

                        if (reader.peek() == JsonToken.NAME) {
                            when (reader.nextName()) {
                                "latlng" -> {
                                    reader.beginArray()
                                    while (reader.hasNext()) {
                                        latlng.add(reader.nextDouble())
                                    }
                                    reader.endArray()
                                }

                                "cca2" -> alpha2 = reader.nextString()
                                "languages" -> {
                                    reader.beginObject()
                                    while (reader.hasNext()) {
                                        languages[reader.nextName()] = reader.nextString()
                                    }
                                    reader.endObject()
                                }

                                "currencies" -> {

                                    reader.beginObject()
                                    while (reader.hasNext()) {
                                        reader.nextName()
                                        reader.beginObject()
                                        while (reader.hasNext()) {
                                            val name = reader.nextName()
                                            if (name == "name") currencies[name] =
                                                CountryWithCurrencyBorderLanguage.Currencies(reader.nextString())
                                            else reader.skipValue()
                                        }
                                        reader.endObject()
                                    }
                                    reader.endObject()
                                }

                                "borders" -> {
                                    reader.beginArray()
                                    while (reader.hasNext()) {
                                        borders.add(reader.nextString())
                                    }
                                    reader.endArray()
                                }

                                else -> reader.skipValue()
                            }
                        } else reader.skipValue()
                    }

                    reader.endObject()

                    countries.add(
                        CountryWithCurrencyBorderLanguage(
                            latlng,
                            alpha2,
                            languages,
                            currencies,
                            borders
                        )
                    )
                }
                reader.endArray()

            }

            return countries
        }

    }

    class CountryWithPopulationTypeAdapter : TypeAdapter<CountryWithPopulationResponse>() {
        override fun read(reader: JsonReader?): CountryWithPopulationResponse {
            val countries = mutableListOf<CountryWithPopulationResponse.CountryWithPopulation>()

            if (reader != null) {
                var countryCode: String? = null
                var population: Long? = null

                reader.beginObject()
                reader.nextName()
                reader.beginObject()
                reader.nextName()
                reader.beginArray()

                while (reader.hasNext()) {
                    reader.beginObject()

                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.NAME) {
                            when (reader.nextName()) {
                                "countryCode" -> countryCode = reader.nextString()
                                "population" -> population = reader.nextLong()
                                else -> reader.skipValue()
                            }


                            if (countryCode != null && population != null) {
                                countries.add(
                                    CountryWithPopulationResponse.CountryWithPopulation(
                                        countryCode,
                                        population
                                    )
                                )

                                countryCode = null
                                population = null
                            }
                        }
                    }

                    reader.endObject()

                }

                reader.endArray()
                reader.endObject()
                reader.endObject()


            }

            return CountryWithPopulationResponse(
                CountryWithPopulationResponse.CountryList(countries)
            )
        }

        override fun write(out: JsonWriter?, value: CountryWithPopulationResponse?) {

        }

    }

}