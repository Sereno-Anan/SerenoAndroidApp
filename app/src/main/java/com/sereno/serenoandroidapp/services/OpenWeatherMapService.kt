package com.sereno.serenoandroidapp.services

import com.sereno.serenoandroidapp.models.apis.openweathermap.currentweatherdata.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {
    @GET("data/2.5/weather/")
    fun fetchWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String?,
        @Query("lang") lang: String?
    ): Call<CurrentWeatherResponse>
}