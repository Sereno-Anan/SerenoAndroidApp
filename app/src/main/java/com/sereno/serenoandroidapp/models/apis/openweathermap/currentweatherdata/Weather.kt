package com.sereno.serenoandroidapp.models.apis.openweathermap.currentweatherdata

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)