package com.welovecrazyquotes.countrylistapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.welovecrazyquotes.countrylistapp.R
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModel
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModelFactory
import com.welovecrazyquotes.countrylistapp.application.CountryListApplication
import com.welovecrazyquotes.countrylistapp.databinding.ActivityCountryDetailBinding
import com.welovecrazyquotes.countrylistapp.databinding.ActivityMainBinding
import com.welovecrazyquotes.countrylistapp.model.Country
import com.welovecrazyquotes.countrylistapp.utils.Constants
import kotlinx.coroutines.launch

class CountryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountryDetailBinding
    private val viewModel: CountriesViewModel by viewModels {
        CountriesViewModelFactory(
            (application as CountryListApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCountryDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        bind()
    }

    private fun bind() {
        val country = intent.getParcelableExtra<Country>(Constants.COUNTRY_COMMON_NAME )
//        Log.d("TAG", "bind: $countryName ")
      lifecycleScope.launchWhenCreated {
          binding.apply {
              country?.let {
                  Log.d("TAG", "bind: $country ")

                  country?.let {
                      Glide.with(this@CountryDetailActivity)
                          .load(country.flags.png)
                          .into(imvFlag)
                      tvCapital.text = "Capital city: ${country.capital[0]}"
                      tvLangs.text = "Languages: ${country.languages}"
                      tvOname.text = "Official name:${ country.name.common }"
                      tvPop.text = "Population: ${country.population}"
                      tvRegion.text = "Region: ${country.region}"
                      tvSregion.text = "Sub-region: ${country.subregion}"

                  }


              }
          }
      }
    }
}