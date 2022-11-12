package com.welovecrazyquotes.countrylistapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Demonyms(

    val eng: Eng,
    val fra: Fra
):Parcelable