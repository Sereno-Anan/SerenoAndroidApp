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
        val moveFirebaseTestButton = findViewById<Button>(R.id.moveFirebaseTestButton)
        val moveWeatherInfoButton = findViewById<Button>(R.id.moveWeatherInfoButton)

        movePlayGifButton.setOnClickListener {
            val intent = Intent(this, PlayGifActivity::class.java)
            startActivity(intent)
        }

        moveFirebaseTestButton.setOnClickListener {
            val intent = Intent(this, FirebaseTestActivity::class.java)
            startActivity(intent)
        }

        moveWeatherInfoButton.setOnClickListener {
            val intent = Intent(this, WeatherInfoActivity::class.java)
            startActivity(intent)
        }
    }
}