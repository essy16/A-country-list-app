package com.welovecrazyquotes.countrylistapp.UI

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModel
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModelFactory
import com.welovecrazyquotes.countrylistapp.application.CountryListApplication
import com.welovecrazyquotes.countrylistapp.databinding.ActivityCountryDetailBinding
import com.welovecrazyquotes.countrylistapp.utils.Constants

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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun bind() {
        lifecycleScope.launchWhenCreated {
            viewModel.countries.collect { countries ->
                val countryName = intent.getStringExtra(Constants.COUNTRY_COMMON_NAME)
                supportActionBar?.title = countryName
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                Log.d("Details", "bind: $countryName ")
                Log.d("Details", "bind: ${countries?.map { it.name.common }} ")
                val country = viewModel.getCountry(countryName ?: "", countries!!)
                Log.d("Details", "bind: $country")
                binding.apply {
                    country?.let {
                        Log.d("TAG", "bind: $country ")

                        country.let {
                            Glide.with(this@CountryDetailActivity)
                                .load(country.flags.png)
                                .into(imvFlag)
                            tvCapital.text = "Capital city: ${country.capital?.get(0).toString()}"
                            tvLangs.text = "Languages: ${country.languages}"
                            tvOname.text = "Official name:${country.name.common}"
                            tvPop.text = "Population: ${country.population}"
                            tvRegion.text = "Region: ${country.region}"
                            tvSregion.text = "Sub-region: ${country.subregion}"

                        }
                    }
                }
            }
        }
    }
}