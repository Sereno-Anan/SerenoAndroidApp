package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sereno.serenoandroidapp.R

class FirebaseTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_test)
        setTitle(R.string.firebase_test_name)
    }
}