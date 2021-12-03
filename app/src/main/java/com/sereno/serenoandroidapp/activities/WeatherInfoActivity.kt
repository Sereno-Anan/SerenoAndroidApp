package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.R

class WeatherInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)

        setTitle(R.string.weather_info_name)
    }
}