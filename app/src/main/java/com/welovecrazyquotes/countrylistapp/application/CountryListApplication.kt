package com.welovecrazyquotes.countrylistapp.application

import android.app.Application
import com.welovecrazyquotes.countrylistapp.Data.RetrofitClient
import com.welovecrazyquotes.countrylistapp.reporsitory.CountryReporsitory

class CountryListApplication : Application() {

    val repository = CountryReporsitory(RetrofitClient.getRestCountryApiService())


}