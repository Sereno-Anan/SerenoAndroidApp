package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.R

class PlayGifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_gif)
        setTitle(R.string.move_test)
    }
}