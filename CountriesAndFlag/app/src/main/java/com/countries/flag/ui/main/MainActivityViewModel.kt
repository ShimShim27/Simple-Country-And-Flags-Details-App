package com.countries.flag.ui.main

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.countries.flag.models.Country
import com.countries.flag.datasource.repo.RestRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class MainActivityViewModel(private val restRepo: RestRepo) : ViewModel() {
    private val countries = mutableListOf<Country>()
    val displayCountries = MutableLiveData(listOf<Country>())
    val showCountryDetails = MutableLiveData<Country?>()
    val countryLoadingFailed = MutableLiveData(false)
    val exitApplication = MutableLiveData(false)
    val showBackPressAgain = MutableLiveData(false)

    var recyclerLayoutParcelable: Parcelable? = null
    private var countriesLoaded = false

    private var backPressed = false

    fun showCountryDetails(country: Country) {
        showCountryDetails.postValue(country)
    }

    fun search(string: String) {
        val toLower = { s: String ->
            s.toLowerCase(Locale.ROOT)
        }
        val toBeSearched = toLower(string)

        val whichCountries = if (string.isEmpty()) countries
        else countries.filter {
            toLower(it.name).contains(string) ||
                    toLower(it.alpha2Code).contains(toBeSearched) ||
                    toLower(it.alpha3Code).contains(toBeSearched)
        }


        displayCountries.postValue(whichCountries)
    }

    fun getCountriesIfNotYet() {
        if (!countriesLoaded) {
            countriesLoaded = true
            CoroutineScope(Dispatchers.Unconfined).launch {
                try {
                    countries.addAll(restRepo.getCountries())
                    displayCountries.postValue(countries)
                } catch (e: Exception) {
                    countryLoadingFailed.postValue(true)
                }

            }
        }

    }


    fun backPressed() {
        if (backPressed) exitApplication.postValue(true)
        else {
            backPressed = true
            showBackPressAgain.postValue(true)
            CoroutineScope(Dispatchers.Unconfined).launch {
                delay(3000)
                backPressed = false
            }
        }
    }
}