package com.welovecrazyquotes.countrylistapp.UI.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.welovecrazyquotes.countrylistapp.model.Country
import com.welovecrazyquotes.countrylistapp.reporsitory.CountryReporsitory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountriesViewModel(private val reporsitory: CountryReporsitory) : ViewModel() {
    private var _countries = MutableStateFlow<List<Country>?>(null)
    val countries = _countries.asStateFlow()
    private var _country = MutableStateFlow<Country?>(null)
    val country = _country.asStateFlow()

    private var countryList = mutableListOf<Country>()


    init {
        getAllCountries()
    }

    private fun getAllCountries() {
        viewModelScope.launch {
            reporsitory.getAllCountries().collect {
                _countries.value = it
                countryList.addAll(it)


            }
        }

    }

    fun getCountry(name: String): Country? {
        return countryList.find {
            it.name.common == name
        }
    }
}

class CountriesViewModelFactory(private val reporsitory: CountryReporsitory) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST_EXCEPTION")
        return if (modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
            CountriesViewModel(reporsitory) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}
