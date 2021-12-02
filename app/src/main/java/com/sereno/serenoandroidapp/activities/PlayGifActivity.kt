package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sereno.serenoandroidapp.R

class PlayGifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_gif)
        setTitle(R.string.play_gif_name)

        val playGifImage = findViewById<ImageView>(R.id.playGifImage)

        Glide
            .with(this)
            .load(R.drawable.rain)
            .into(playGifImage)
    }
}