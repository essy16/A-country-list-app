package com.welovecrazyquotes.countrylistapp.reporsitory

import com.welovecrazyquotes.countrylistapp.Data.RestcountryAPIservice
import com.welovecrazyquotes.countrylistapp.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CountryRepository (private val api:RestcountryAPIservice){
    fun getAllCountries():Flow<Response<List<Country>>>{
        return flow {
            emit(api.getAllCountries())

        }
    }

}