package com.welovecrazyquotes.countrylistapp.UI.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.welovecrazyquotes.countrylistapp.model.Country
import com.welovecrazyquotes.countrylistapp.model.FilterString
import com.welovecrazyquotes.countrylistapp.reporsitory.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountriesViewModel(private val reporsitory: CountryRepository) : ViewModel() {
    private var _countries = MutableStateFlow<List<Country>?>(emptyList())
    val countries = _countries.asStateFlow()
    private var _country = MutableStateFlow<Country?>(null)
    val country = _country.asStateFlow()
    var continents = mutableListOf<FilterString>()
    var timeZones = mutableListOf<FilterString>()
    var selectedFilters = mutableListOf<FilterString>()
    private var countryList = mutableListOf<Country>()
    var isLoading = MutableStateFlow<Boolean>(false)
    var isServerError = MutableStateFlow<Boolean>(false)
    var isNetworkError = MutableStateFlow<Boolean>(false)


    init {
        getAllCountries()
    }

    private fun getAllCountries() {
        viewModelScope.launch {
            isLoading.value = true
            reporsitory.getAllCountries().collect { countriesResponse ->
                try {
                    if (countriesResponse.isSuccessful){
                        val response = countriesResponse.body()?.sortedBy { country -> country.name.common }
                        _countries.value = response
                        if (response != null) {
                            countryList.addAll(response)
                            isLoading.value = false
                            isNetworkError.value = false
                            isServerError.value = false
                            continents = response.map { country ->
                                FilterString(stringFilter = country.region, selected = false)
                            }.distinct().toMutableList()
                        }
                        Log.d("TAG", "getAllCountries: $countryList")
                    }else{
                        isLoading.value = false
                        isNetworkError.value = false
                        isServerError.value = true
                    }
                }catch (e:Exception){
                    isLoading.value = false
                    isNetworkError.value = true
                    isServerError.value = false
                }


            }
        }

    }

    fun getCountry(name: String, countries:List<Country>): Country? {
        return countries.find {
            it.name.common.lowercase() == name.lowercase()
        }
    }

    fun resetFilters(){
        for (continent in selectedFilters){
            continent.selected = false
        }
    }
    init {
        getAllCountries()
    }
}

class CountriesViewModelFactory(private val reporsitory: CountryRepository) :
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
