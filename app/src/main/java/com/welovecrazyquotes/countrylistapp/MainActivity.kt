package com.welovecrazyquotes.countrylistapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.animation.AnimationUtils

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val img_btn=findViewById<ImageView>(R.id.image_button)
        val appSettingPreferences: SharedPreferences =getSharedPreferences("AppSettingPrefernces",0)
        val sharedPrefEdit: SharedPreferences.Editor=appSettingPreferences.edit()
        val isNightModeOn:Boolean=appSettingPreferences.getBoolean("Nightmode",false)
        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        img_btn.setOnClickListener({
            if (isNightModeOn) {
img_btn.setImageResource(R.drawable.sun_light)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("Nightmode",false)
                sharedPrefEdit.apply()

            }
            else{
                img_btn.setImageResource(R.drawable.moon_dark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("Nightmode",true)
                sharedPrefEdit.apply()
            }

        })


    }

}