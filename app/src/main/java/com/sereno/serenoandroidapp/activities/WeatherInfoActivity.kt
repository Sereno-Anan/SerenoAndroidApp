package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.R

class WeatherInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)
        val getWeatherButton = findViewById<Button>(R.id.getWeatherButton)

        setTitle(R.string.weather_info_name)
    }
}