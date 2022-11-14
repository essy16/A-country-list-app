package com.welovecrazyquotes.countrylistapp.UI

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.welovecrazyquotes.countrylistapp.Adapter.CountryAdapter
import com.welovecrazyquotes.countrylistapp.R
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModel
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModelFactory
import com.welovecrazyquotes.countrylistapp.application.CountryListApplication
import com.welovecrazyquotes.countrylistapp.databinding.ActivityMainBinding
import com.welovecrazyquotes.countrylistapp.model.Country
import com.welovecrazyquotes.countrylistapp.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var countryListAdapter: CountryAdapter
    private lateinit var SearchView: SearchView
    private var countryList = mutableListOf<Country>()

    private var regionNames = mutableListOf<String>()
    private val viewModel: CountriesViewModel by viewModels {
        CountriesViewModelFactory(
            (application as CountryListApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        darkMode()
        bind()

    }

    private fun darkMode() {
        val img_btn = findViewById<ImageView>(R.id.image_button)
        val appSettingPreferences: SharedPreferences =
            getSharedPreferences("AppSettingPrefernces", 0)
        val sharedPrefEdit: SharedPreferences.Editor = appSettingPreferences.edit()
        val isNightModeOn: Boolean = appSettingPreferences.getBoolean("Nightmode", false)
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        img_btn.setOnClickListener {
            if (isNightModeOn) {
                img_btn.setImageResource(R.drawable.sun_light)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("Nightmode", false)
                sharedPrefEdit.apply()

            } else {
                img_btn.setImageResource(R.drawable.moon_dark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("Nightmode", true)
                sharedPrefEdit.apply()
            }

        }


    }


    private fun bind() {
        countryListAdapter = CountryAdapter(this) {

            viewModel.getCountry(it.name.common)

            val intent = Intent(this@MainActivity, CountryDetailActivity::class.java).apply {
                putExtra(Constants.COUNTRY_COMMON_NAME, it)

            }
            startActivity(intent)
        }
        lifecycleScope.launchWhenCreated {
            viewModel.countries.collect {
                it?.let {
                    countryListAdapter.submitList(it.sortedBy { country ->
                        country.name.common
                    })
                    Log.d("TAG", "bind: $it")
                    it.map { countryList ->

                        regionNames.addAll(countryList.continents)

                    }

                }
            }
        }
        binding.recyclerCountries.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            adapter = countryListAdapter

        }
        binding.layoutFilter.setOnClickListener {
            var chckAF = findViewById<CheckBox>(R.id.Africa)
            var chckAS = findViewById<CheckBox>(R.id.Asia)
            var chckASTF = findViewById<CheckBox>(R.id.Austarlia)
            var chckER = findViewById<CheckBox>(R.id.Europe)
            var chckNA = findViewById<CheckBox>(R.id.NAmerica)
            var chckSA = findViewById<CheckBox>(R.id.SAmerica)
            var chckANT = findViewById<CheckBox>(R.id.Antartica)
            if (chckAF.isChecked) {


            }
            if (chckAS.isChecked) {


            }

            if (chckASTF.isChecked) {


            }

            if (chckER.isChecked) {


            }

            if (chckNA.isChecked) {


            }

            if (chckSA.isChecked) {


            }
            if (chckANT.isChecked) {


            }


        }

        fun toggle(region: String) {
            lifecycleScope.launchWhenCreated {
                viewModel.countries.collect {
                    it?.let {
                        Log.d("TAG", "bind: $it")
                        it.filter { country ->
                            country.region == region

                        }.also {
                            countryListAdapter.submitList(it)
                        }

                    }
                }
            }


        }

    private fun searchCountry() {
        SearchView = findViewById(R.id.search_view)
        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
               val filteredList=countryList.filter { country ->
                   country.name.common.lowercase()  }
                if (query.isNotEmpty()){
                    countryListAdapter.submitList(filteredList)
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                co.filter.filter(newText)
                return false
            }
        })
    }
    }