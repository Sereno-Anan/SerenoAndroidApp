package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        val getWeatherButton = findViewById<Button>(R.id.get)

        setTitle(R.string.weather_info_name)
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

        val cityNameView = findViewById<EditText>(R.id.inputCityName)
        val cityNameText = cityNameView.text.toString()

        thread {
            try {
                val service: OpenWeatherMapService =
                    retrofit.create(OpenWeatherMapService::class.java)
                val weatherApiResponse = service.getCurrentWeatherData(
                    cityNameText, BuildConfig.OWM_API_KEY, "metric", "ja"
                ).execute().body()
                    ?: throw IllegalStateException()

                Log.d("city-name", cityNameText)

                Handler(Looper.getMainLooper()).post {
                    Log.d("response-weather", weatherApiResponse.weather.toString())
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