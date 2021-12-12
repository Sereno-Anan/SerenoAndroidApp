package com.sereno.serenoandroidapp.activities

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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

class WeatherInfoActivity: AppCompatActivity(), SensorEventListener {
    var lastUpdate: Long = System.currentTimeMillis()
    val SHAKE_THRESHOLD: Int = 750
    var last_x = 0f
    var last_y = 0f
    var last_z = 0f
    var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)

        val getWeatherButton = findViewById<Button>(R.id.getCurrentWeatherButton)
        val pref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        setTitle(R.string.weather_info_name)
        if (pref.getString("cityName", "")!!.isNotEmpty()) {
            getCurrentWeather()
        }
        getWeatherButton.setOnClickListener {
            getCurrentWeather()
        }
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
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
        val currentCityNameText = findViewById<TextView>(R.id.currentCityName)
        val pref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val cityName = if (inputCityNameText.text.toString() != "") {
            inputCityNameText.text.toString()
        } else {
            pref.getString("cityName", "").toString()
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
                    getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit().apply {
                        putString("cityName", cityName)
                        apply()
                    }
                    currentCityNameText.text = pref.getString("cityName", "")
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

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == SensorManager.SENSOR_ACCELEROMETER as Sensor) {
            val curTime: Long = System.currentTimeMillis()
            if ((curTime - lastUpdate) > 100) {
                val diffTime: Long = (curTime - lastUpdate)
                lastUpdate = curTime
                val x = event.values[SensorManager.DATA_X]
                val y = event.values[SensorManager.DATA_Y]
                val z = event.values[SensorManager.DATA_Z]
                val speed: Float = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000
                if (speed > SHAKE_THRESHOLD) {
                    val intent = Intent(this, FirebaseTestActivity::class.java)
                    startActivity(intent)
                }
                last_x = x
                last_y = y
                last_z = z
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
}