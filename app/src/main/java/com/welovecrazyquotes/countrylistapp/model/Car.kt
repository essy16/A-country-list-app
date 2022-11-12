package com.welovecrazyquotes.countrylistapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val side: String,
    val signs: List<String>
):Parcelable