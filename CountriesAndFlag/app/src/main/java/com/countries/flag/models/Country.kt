package com.countries.flag.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Country(
    @SerializedName("name")
    val name: String,
    @SerializedName("topLevelDomain")
    val topLevelDomain: List<String>,
    @SerializedName("alpha2Code")
    val alpha2Code: String,
    @SerializedName("alpha3Code")
    val alpha3Code: String,
    @SerializedName("callingCodes")
    val callingCodes: List<String>,
    @SerializedName("capital")
    val capital: String,
    @SerializedName("altSpellings")
    val altSpellings: List<String>,
    @SerializedName("region")
    val region: String,
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double,
    @SerializedName("currencies")
    var currencies: List<String>,
    @SerializedName("languages")
    var languages: List<String>,
    @SerializedName("borders")
    var borders: List<String>,
    @SerializedName("population")
    var population: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(topLevelDomain)
        parcel.writeString(alpha2Code)
        parcel.writeString(alpha3Code)
        parcel.writeStringList(callingCodes)
        parcel.writeString(capital)
        parcel.writeStringList(altSpellings)
        parcel.writeString(region)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeStringList(currencies)
        parcel.writeStringList(languages)
        parcel.writeStringList(borders)
        parcel.writeLong(population)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}