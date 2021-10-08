package com.countries.flag.dagger

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.countries.flag.config.CONFIG
import com.countries.flag.datasource.retrofit.CountryLayerRestApi
import com.countries.flag.datasource.retrofit.GithubRestApi
import com.countries.flag.datasource.repo.RestRepo
import com.countries.flag.datasource.room.database.MainDatabase
import com.countries.flag.ui.main.MainActivityViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ViewModelModule(private val context: Context) {

    @Provides
    fun providesRestRepo(): RestRepo {
        val countryLayerApi = Retrofit.Builder().baseUrl(CONFIG.COUNTRY_LAYER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryLayerRestApi::class.java)

        val rawGithubApi = Retrofit.Builder().baseUrl(CONFIG.RAW_GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubRestApi::class.java)

        val gistGithubApi = Retrofit.Builder().baseUrl(CONFIG.GIST_GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubRestApi::class.java)

        return RestRepo(
            MainDatabase.getInstance(context),
            countryLayerApi,
            rawGithubApi,
            gistGithubApi
        )
    }


    @Provides
    fun providesMainActivityViewModel(repo: RestRepo): MainActivityViewModel {
        return ViewModelProvider(context as ViewModelStoreOwner,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainActivityViewModel(repo) as T
                }

            }).get(MainActivityViewModel::class.java)
    }
}