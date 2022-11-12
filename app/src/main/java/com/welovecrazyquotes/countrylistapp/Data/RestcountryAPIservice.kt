package com.welovecrazyquotes.countrylistapp.Data

import com.welovecrazyquotes.countrylistapp.model.Country
import retrofit2.http.GET

interface RestcountryAPIservice {
    @GET("all")
  suspend  fun getAllCountries(): List<Country>

}
