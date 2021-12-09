package com.sereno.serenoandroidapp.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sereno.serenoandroidapp.BuildConfig
import com.sereno.serenoandroidapp.R
import com.sereno.serenoandroidapp.services.OpenWeatherMapService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.concurrent.thread

class WeatherInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)
        val getWeatherButton = findViewById<Button>(R.id.getCurrentWeatherButton)
        val pref = getSharedPreferences("my_settings", Context.MODE_PRIVATE)

        setTitle(R.string.weather_info_name)
        if (pref.getString("keepCityName", "")!!.isEmpty()) {
            getCurrentWeather()
        }
        getWeatherButton.setOnClickListener {
            getCurrentWeather()
        }
    }

    private fun getCurrentWeather() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val inputCityNameText = findViewById<EditText>(R.id.inputCityNameText)
        val currentWeatherIcon = findViewById<ImageView>(R.id.outputWeatherIcon)
        val currentWeatherText = findViewById<TextView>(R.id.outputWeatherText)
        val pref = getSharedPreferences("CityNameData", Context.MODE_PRIVATE)
        val cityName = if (inputCityNameText.text.toString() != "") {
            inputCityNameText.text.toString()
        } else {
            pref.getString("keepCityName", "").toString()
        }


        thread {
            try {
                val service: OpenWeatherMapService =
                    retrofit.create(OpenWeatherMapService::class.java)
                val weatherApiResponse = service.getCurrentWeatherData(
                    cityName, BuildConfig.OWM_API_KEY, "metric", "ja"
                ).execute().body()
                    ?: throw IllegalStateException("body is null")

                Log.d("city-name", cityName)

                Handler(Looper.getMainLooper()).post {
                    Log.d("response-weather", weatherApiResponse.weather.toString())
                    val weatherIconUrl = weatherApiResponse.weather[0].icon
                    val currentTemp = weatherApiResponse.main.temp.toString()
                    currentWeatherText.text = currentTemp
                    Glide.with(this)
                        .load("https://openweathermap.org/img/wn/$weatherIconUrl@2x.png")
                        .into(currentWeatherIcon)
                    getSharedPreferences("CityNameData", Context.MODE_PRIVATE).edit().apply {
                        putString("keepCityName", cityName)
                        apply()
                    }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    val toast =
                        Toast.makeText(this, "This city name is not found", Toast.LENGTH_SHORT)
                    toast.show()
                }
                Log.d("response-error", "debug $e")
            }
        }
    }
}