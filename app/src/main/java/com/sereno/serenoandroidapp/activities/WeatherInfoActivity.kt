package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        setTitle(R.string.weather_info_name)


        getCurrentWeather()
    }

    private fun getCurrentWeather() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        thread {
            try {
                val service: OpenWeatherMapService =
                    retrofit.create(OpenWeatherMapService::class.java)
                val weatherApiResponse = service.getCurrentWeatherData(
                    "anan", BuildConfig.OWM_API_KEY, "metric", "ja"
                ).execute().body()
                    ?: throw IllegalStateException("bodyがnull")

                Handler(Looper.getMainLooper()).post {
                    Log.d("response-weather", weatherApiResponse.weather.toString())
                }
            } catch (e: Exception) {
                Log.d("response-weather", "debug $e")
            }
        }
    }
}