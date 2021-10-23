package com.countries.flag.ui.countrydetails

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.countries.flag.models.Country
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CountryDetailsActivityViewModel : ViewModel() {

    val displayMarker = MutableLiveData<MarkerOptions?>()
    val moveCamera = MutableLiveData<CameraUpdate?>()
    val countryDetailsCopied = MutableLiveData(false)

    fun setMapMarker(latitude: Double, longitude: Double) {
        val latlng = LatLng(latitude, longitude)
        displayMarker.postValue(MarkerOptions().position(latlng))
    }

    fun moveCameraMap(latitude: Double, longitude: Double) {
        val latlng = LatLng(latitude, longitude)
        moveCamera.postValue(CameraUpdateFactory.newLatLng(latlng))
    }


    fun toggleMapType(googleMap: GoogleMap?) {
        if (googleMap != null) {
            val satellite = GoogleMap.MAP_TYPE_SATELLITE
            val normal = GoogleMap.MAP_TYPE_NORMAL
            googleMap.mapType = if (googleMap.mapType == normal) satellite else normal
        }

    }

    fun copyCountryDetails(clipboardManager: ClipboardManager, country: Country) {
        var details = "Name: %s\n" +
                "Capital: %s\n" +
                "Region: %s\n" +
                "Alpha 2 Code: %s\n" +
                "Alpha 3 Code: %s\n" +
                "Calling Codes: %s\n" +
                "Alt Spellings: %s\n" +
                "Latitude: %s\n" +
                "Longitude: %s\n" +
                "Languages: %s\n" +
                "Currencies: %s\n" +
                "Borders: %s\n" +
                "Population: %s\n"

        details = String.format(
            details,
            country.name,
            country.capital,
            country.region,
            country.alpha2Code,
            country.alpha3Code,
            detailsListToString(country.callingCodes),
            detailsListToString(country.altSpellings),
            country.latitude,
            country.longitude,
            detailsListToString(country.languages),
            detailsListToString(country.currencies),
            detailsListToString(country.borders),
            country.population
        )


        clipboardManager.setPrimaryClip(ClipData.newPlainText("country details", details))
        countryDetailsCopied.postValue(true)
    }


    fun detailsListToString(details: List<String>): String {
        var newStrings = ""
        details.forEach { newStrings += "$it , " }
        if (newStrings.isNotEmpty()) newStrings =
            newStrings.substring(0, newStrings.length - 3)

        return newStrings
    }
}