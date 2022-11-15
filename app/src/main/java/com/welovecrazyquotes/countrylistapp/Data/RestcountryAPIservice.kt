package com.welovecrazyquotes.countrylistapp.Data

import com.welovecrazyquotes.countrylistapp.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface RestcountryAPIservice {
    @GET("all")
    suspend  fun getAllCountries(): Response<List<Country>>

}
