package com.welovecrazyquotes.countrylistapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Slk(
    val common: String,
    val official: String
):Parcelable