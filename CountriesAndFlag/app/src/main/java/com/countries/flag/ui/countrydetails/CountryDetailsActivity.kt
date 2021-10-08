package com.countries.flag.ui.countrydetails

import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.countries.flag.R
import com.countries.flag.models.Country
import com.countries.flag.ui.util.MainUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class CountryDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val EXTRA_COUNTRY = "country"
    }

    private lateinit var country: Country
    private lateinit var viewModel: CountryDetailsActivityViewModel

    private lateinit var mapFragment: FragmentContainerView
    private lateinit var countryImagePreview: ImageView
    private lateinit var countryNameView: TextView
    private lateinit var capitalView: TextView
    private lateinit var regionView: TextView
    private lateinit var alpha2CodeView: TextView
    private lateinit var alpha3CodeView: TextView
    private lateinit var callingCodesView: TextView
    private lateinit var altSpellingsView: TextView
    private lateinit var latitudeView: TextView
    private lateinit var longitudeView: TextView
    private lateinit var languagesView: TextView
    private lateinit var currenciesView: TextView
    private lateinit var bordersView: TextView
    private lateinit var populationView: TextView
    private var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)



        mapFragment = findViewById(R.id.mapFragment)
        countryImagePreview = findViewById(R.id.countryImagePreview)
        countryNameView = findViewById(R.id.countryNameView)
        capitalView = findViewById(R.id.capitalView)
        regionView = findViewById(R.id.regionView)
        alpha2CodeView = findViewById(R.id.alpha2CodeView)
        alpha3CodeView = findViewById(R.id.alpha3CodeView)
        callingCodesView = findViewById(R.id.callingCodesView)
        altSpellingsView = findViewById(R.id.altSpellingsView)
        latitudeView = findViewById(R.id.latitudeView)
        longitudeView = findViewById(R.id.longitudeView)
        languagesView = findViewById(R.id.languagesView)
        currenciesView = findViewById(R.id.currenciesView)
        bordersView = findViewById(R.id.bordersView)
        populationView = findViewById(R.id.populationView)

        country = intent!!.extras!!.getParcelable(EXTRA_COUNTRY)!!
        supportActionBar?.title = country.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        initViewModel()
        initViews()
        initMapView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.country_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.copyCountryDetails -> viewModel.copyCountryDetails(
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager,
                country
            )

            R.id.focusOnCountry -> viewModel.moveCameraMap(
                country.latitude,
                country.longitude
            )

            R.id.toggleMapType -> viewModel.toggleMapType(googleMap)
        }
        return true
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        viewModel.moveCameraMap(country.latitude, country.longitude)
        viewModel.setMapMarker(country.latitude, country.longitude)
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CountryDetailsActivityViewModel::class.java)
        viewModel.displayMarker.observe(this, {
            if (it != null) {
                viewModel.displayMarker.value = null
                googleMap?.addMarker(it)
            }
        })

        viewModel.moveCamera.observe(this, {
            if (it != null) {
                viewModel.moveCamera.value = null
                googleMap?.moveCamera(it)
            }
        })

        viewModel.countryDetailsCopied.observe(this, {
            if (it) {
                viewModel.countryDetailsCopied.value = false
                MainUtil.makeToast(this, getString(R.string.details_copied))
            }
        })
    }

    private fun initViews() {
        MainUtil.loadImageUrl(
            this,
            Uri.parse(MainUtil.getFlagsLink(country.alpha2Code)),
            countryImagePreview
        )



        countryNameView.text = country.name
        capitalView.text = country.capital
        regionView.text = country.region
        alpha2CodeView.text = country.alpha2Code
        alpha3CodeView.text = country.alpha3Code
        callingCodesView.text = viewModel.detailsListToString(country.callingCodes)
        altSpellingsView.text = viewModel.detailsListToString(country.altSpellings)
        latitudeView.text = country.latitude.toString()
        longitudeView.text = country.longitude.toString()
        languagesView.text = viewModel.detailsListToString(country.languages)
        currenciesView.text = viewModel.detailsListToString(country.currencies)
        bordersView.text = viewModel.detailsListToString(country.borders)
        populationView.text = country.population.toString()
    }

    private fun initMapView() {
        (supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment)
            .getMapAsync(this)
    }


}