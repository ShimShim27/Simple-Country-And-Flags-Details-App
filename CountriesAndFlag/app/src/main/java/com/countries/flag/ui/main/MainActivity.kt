package com.countries.flag.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.countries.flag.R
import com.countries.flag.dagger.DaggerViewModelComponent
import com.countries.flag.dagger.ViewModelModule
import com.countries.flag.ui.countrydetails.CountryDetailsActivity
import com.countries.flag.ui.util.MainUtil
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var countriesRecyclerView: RecyclerView
    private lateinit var countriesRecyclerAdapter: CountriesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countriesRecyclerView = findViewById(R.id.countriesRecyclerView)

        initViewModel()

        countriesRecyclerAdapter = CountriesRecyclerAdapter(viewModel)
        countriesRecyclerView.adapter = countriesRecyclerAdapter
        countriesRecyclerView.addItemDecoration(CountriesRecyclerDecoration())
        countriesRecyclerView.layoutManager?.onRestoreInstanceState(viewModel.recyclerLayoutParcelable)
        viewModel.getCountriesIfNotYet()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        (menu?.findItem(R.id.searchView)!!.actionView as SearchView).apply {
            queryHint = getString(R.string.search_view_hint)
            imeOptions = EditorInfo.IME_ACTION_NONE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) viewModel.search(newText)
                    return true
                }

            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.recyclerLayoutParcelable =
            countriesRecyclerView.layoutManager?.onSaveInstanceState()
    }

    private fun initViewModel() {
        viewModel =
            DaggerViewModelComponent.builder().viewModelModule(ViewModelModule(this)).build()
                .getMainActivityViewModel()
        viewModel.displayCountries.observe(this, {
            countriesRecyclerAdapter.submitList(ArrayList(it))
        })

        viewModel.showCountryDetails.observe(this, { country ->
            if (country != null) {
                viewModel.showCountryDetails.value = null
                startActivity(Intent(this, CountryDetailsActivity::class.java).apply {
                    putExtra(CountryDetailsActivity.EXTRA_COUNTRY, country)
                })
            }
        })

        viewModel.countryLoadingFailed.observe(this, {
            if (it) {
                viewModel.countryLoadingFailed.value = false
                MainUtil.makeToast(this, getString(R.string.country_loading_failed))
            }
        })

        viewModel.exitApplication.observe(this, {
            if (it) {
                viewModel.exitApplication.value = false
                finishAffinity()
            }
        })

        viewModel.showBackPressAgain.observe(this, {
            if (it) {
                viewModel.showBackPressAgain.value = false
                MainUtil.makeToast(this, getString(R.string.back_press_again))
            }
        })
    }


}