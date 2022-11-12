package com.welovecrazyquotes.countrylistapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Name(
    val common: String,
    val nativeName: NativeName,
    val official: String
):Parcelable