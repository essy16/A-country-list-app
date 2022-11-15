package com.welovecrazyquotes.countrylistapp.application

import android.app.Application
import com.welovecrazyquotes.countrylistapp.Data.RetrofitClient
import com.welovecrazyquotes.countrylistapp.reporsitory.CountryRepository

class CountryListApplication : Application() {

    val repository = CountryRepository(RetrofitClient.getRestCountryApiService())


}