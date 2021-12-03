package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sereno.serenoandroidapp.R
import com.sereno.serenoandroidapp.models.db.firestore.RainDrops

class FirebaseTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_test)
        setTitle(R.string.firebase_test_name)

        val rootLayout: View = findViewById(android.R.id.content)

        val db = FirebaseFirestore.getInstance()
        val addFirebaseDataButton = findViewById<Button>(R.id.addFirebaseDataButton)

        addFirebaseDataButton.setOnClickListener {
            db.collection("raindrops")
                .document()
                .set(RainDrops(true, FieldValue.serverTimestamp()))
                .addOnSuccessListener {
                    Log.i("Firebase Log", "Success")
                    Snackbar.make(rootLayout, "Success", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Log.w("Firebase Log", "Error sending documents.", it)
                    Snackbar.make(rootLayout, "Failure", Snackbar.LENGTH_LONG).show()
                }
        }
    }
}