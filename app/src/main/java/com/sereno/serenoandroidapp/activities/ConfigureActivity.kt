package com.sereno.serenoandroidapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.sereno.serenoandroidapp.BuildConfig
import com.sereno.serenoandroidapp.R
import com.sereno.serenoandroidapp.services.OpenWeatherMapService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.concurrent.thread

class ConfigureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure)
        setTitle("Setting")

        val setCityNameText = findViewById<EditText>(R.id.setCityNameText)
        val setButton = findViewById<Button>(R.id.setCityNameButton)

        setButton.setOnClickListener {
            setCityName(setCityNameText.text.toString())
        }
    }

    fun setCityName(cityName: String) {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val rootLayout: View = findViewById(android.R.id.content)

        thread {
            try {
                val service: OpenWeatherMapService =
                    retrofit.create(OpenWeatherMapService::class.java)
                val weatherApiResponse = service.getCurrentWeatherData(
                    cityName, BuildConfig.OWM_API_KEY, "metric", "ja"
                ).execute().body()
                    ?: throw IllegalStateException("body is null")

                getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit().apply {
                    putString("widgetCityName", cityName)
                    apply()
                }
            } catch (e: Exception) {
                Snackbar.make(rootLayout, "This city name is not found", Snackbar.LENGTH_LONG).show()

                Log.d("response-error", "debug $e")
            }
        }
    }
}