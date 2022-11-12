package com.welovecrazyquotes.countrylistapp.reporsitory

import com.welovecrazyquotes.countrylistapp.Data.RestcountryAPIservice
import com.welovecrazyquotes.countrylistapp.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryReporsitory (private val api:RestcountryAPIservice){
    fun getAllCountries():Flow<List<Country>>{
        return flow {
            emit(api.getAllCountries())

        }
    }

}