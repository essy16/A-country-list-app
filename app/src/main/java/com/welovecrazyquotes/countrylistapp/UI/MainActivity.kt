package com.welovecrazyquotes.countrylistapp.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.welovecrazyquotes.countrylistapp.Adapter.CountryAdapter
import com.welovecrazyquotes.countrylistapp.Adapter.FilterAdapter
import com.welovecrazyquotes.countrylistapp.R
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModel
import com.welovecrazyquotes.countrylistapp.UI.viewmodel.CountriesViewModelFactory
import com.welovecrazyquotes.countrylistapp.application.CountryListApplication
import com.welovecrazyquotes.countrylistapp.databinding.ActivityMainBinding
import com.welovecrazyquotes.countrylistapp.utils.Constants
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var countryListAdapter: CountryAdapter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    var continentsIsExpanded:Boolean = true
    var timeZoneIsExpanded:Boolean = true

    private var regionNames = mutableListOf<String>()
    private val viewModel: CountriesViewModel by viewModels {
        CountriesViewModelFactory(
            (application as CountryListApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.filterSheet)

        supportActionBar?.hide()

        setContentView(binding.root)
        darkMode()
        bind()

    }

    private fun darkMode() {
        val appSettingPreferences: SharedPreferences =
            getSharedPreferences("AppSettingPrefernces", 0)
        val sharedPrefEdit: SharedPreferences.Editor = appSettingPreferences.edit()
        val isNightModeOn: Boolean = appSettingPreferences.getBoolean("Nightmode", false)
        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.imageButton.setImageResource(R.drawable.ic_dark_mode)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.imageButton.setImageResource(R.drawable.ic_light_mode )

        }
        binding.imageButton.setOnClickListener {
            if (isNightModeOn) {
                binding.imageButton.setImageResource(R.drawable.ic_light_mode )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("Nightmode", false)
                sharedPrefEdit.apply()

            } else {
                binding.imageButton.setImageResource(R.drawable.ic_dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("Nightmode", true)
                sharedPrefEdit.apply()
            }
        }


    }


    private fun bind() {
        countryListAdapter = CountryAdapter(this) {
            val intent = Intent(this@MainActivity, CountryDetailActivity::class.java).apply {
                putExtra(Constants.COUNTRY_COMMON_NAME, it.name.common)

            }
            startActivity(intent)
        }
        filterAdapter = FilterAdapter{ filter, isChecked ->
            if (isChecked){
                filter.selected = true
                if (!viewModel.selectedFilters.contains(filter))
                viewModel.selectedFilters.add(filter)
                Log.d("Main", "selected: ${viewModel.selectedFilters} ")
            }else {
                viewModel.selectedFilters.remove(filter)
                Log.d("Main", "selected: ${viewModel.selectedFilters} ")
            }
        }
        filterAdapter.submitList(viewModel.continents)
        Log.d("Main", "size: ${filterAdapter.itemCount}")
        binding.rvContinents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = filterAdapter
        }

        binding.continentsToggle.setOnClickListener {
            if (continentsIsExpanded) {
            Log.d("Continents", "bind: ${viewModel.continents}")
            binding.rvContinents.visibility = View.VISIBLE
                binding.continentsToggle
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,0, R.drawable.ic_arrow_up, 0,
                    )
                continentsIsExpanded = false
            }else{

                binding.rvContinents.visibility = View.GONE
                binding.continentsToggle
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                         0,  0, R.drawable.ic_arrow_down, 0  )
                continentsIsExpanded = true
            }
        }

        binding.timeZoneToggle.setOnClickListener {
            if (timeZoneIsExpanded) {
                Log.d("Continents", "bind: ${viewModel.continents}")
                binding.rvTimeZones.visibility = View.VISIBLE
                binding.continentsToggle
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,0, R.drawable.ic_arrow_up, 0,
                    )
                timeZoneIsExpanded = false
            }else{

                binding.rvTimeZones.visibility = View.GONE
                binding.continentsToggle
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,  0, R.drawable.ic_arrow_down, 0  )
                timeZoneIsExpanded = true
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isNetworkError.collect{ isNetworkError ->
                if (isNetworkError) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "No internet connection. Check and try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    viewModel.isLoading.collect{ isLoading ->
                        if (isLoading){
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerCountries.visibility = View.GONE
                        }else{
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerCountries.visibility = View.VISIBLE
                            viewModel.countries.collect {
                                it?.let {
                                    countryListAdapter.submitList(it.sortedBy { country ->
                                        country.name.common
                                    })
                                    Log.d("TAG", "bind: $it")
                                    it.map { countryList ->
                                        countryList.continents.let { it1 -> regionNames.addAll(it1) }
                                    }

                                }

                                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                                    override fun onQueryTextSubmit(query: String?): Boolean {
                                        val filteredList = it?.filter { country ->
                                            country.name.common.lowercase()
                                                .contains(query!!.lowercase(Locale.getDefault()))
                                        }
                                        countryListAdapter.submitList(null)
                                        countryListAdapter.submitList(filteredList)
                                        return true
                                    }

                                    override fun onQueryTextChange(newText: String?): Boolean {
                                        val filteredList = it?.filter { country ->
                                            country.name.common.lowercase()
                                                .contains(newText!!.lowercase(Locale.getDefault()))
                                        }
                                        countryListAdapter.submitList(null)
                                        countryListAdapter.submitList(filteredList)
                                        return true
                                    }

                                })
                            }
                        }



                    }
                }

        }






        }
        binding.recyclerCountries.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countryListAdapter

        }
        binding.searchView.apply {
            //requestFocusFromTouch()
            setIconifiedByDefault(false)
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

        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = 0
        }
        binding.btnFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.btnReset.setOnClickListener {
            viewModel.resetFilters()
            viewModel.selectedFilters.clear()
            lifecycleScope.launch {
                viewModel.countries.collect { countries ->
                    countryListAdapter.submitList(countries)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
        binding.closeFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.btnShowResults.setOnClickListener {
            lifecycleScope.launch {
                viewModel.countries.collect { countries ->
                    val filters = viewModel.selectedFilters.map { it.stringFilter }
                    val filtered = countries?.filter {
                        filters.contains(it.region)
                    }
                    countryListAdapter.submitList(filtered)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }


        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState== BottomSheetBehavior.STATE_DRAGGING){
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
             Unit
            }

        })

        fun toggle(region: String) {
            lifecycleScope.launchWhenCreated {
                viewModel.countries.collect { countries ->
                    countries?.let { countryList ->
                        countryList.filter { country ->
                            country.region == region
                        }.also {
                            countryListAdapter.submitList(countries)
                        }

                    }
                }
            }


        }



    }
}