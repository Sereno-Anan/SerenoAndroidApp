package com.sereno.serenoandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.activities.FirebaseTestActivity
import com.sereno.serenoandroidapp.activities.PlayGifActivity
import com.sereno.serenoandroidapp.activities.WeatherInfoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movePlayGifButton = findViewById<Button>(R.id.movePlayGifButton)
        val moveFirebaseTest = findViewById<Button>(R.id.moveFirebaseTestButton)
        val moveWeather = findViewById<Button>(R.id.moveWeather)

        movePlayGifButton.setOnClickListener {
            val intent = Intent(this, PlayGifActivity::class.java)
            startActivity(intent)
        }

        moveFirebaseTest.setOnClickListener {
            val intent = Intent(this, FirebaseTestActivity::class.java)
            startActivity(intent)
        }

        moveWeather.setOnClickListener {
            val intent = Intent(this, WeatherInfoActivity::class.java)
            startActivity(intent)
        }
    }
}