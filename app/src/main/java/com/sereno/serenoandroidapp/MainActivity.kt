package com.sereno.serenoandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.activities.PlayGifActivity
import com.sereno.serenoandroidapp.activities.WeatherInfoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moveBtn = findViewById<Button>(R.id.movePlayGifButton)
        val moveWeather = findViewById<Button>(R.id.moveWeather)

        moveBtn.setOnClickListener {
            val intent = Intent(this, PlayGifActivity::class.java)
            startActivity(intent)
        }

        moveWeather.setOnClickListener {
            val intent = Intent(this, WeatherInfoActivity::class.java)
            startActivity(intent)
        }
    }
}