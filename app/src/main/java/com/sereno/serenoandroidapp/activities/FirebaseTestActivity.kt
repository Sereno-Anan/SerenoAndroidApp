package com.sereno.serenoandroidapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sereno.serenoandroidapp.R
import com.sereno.serenoandroidapp.models.db.firestore.RainDrops
import java.text.SimpleDateFormat
import java.util.*

class FirebaseTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_test)
        setTitle(R.string.firebase_test_name)

        val rootLayout: View = findViewById(android.R.id.content)

        val firestore = FirebaseFirestore.getInstance()

        val addFirebaseDataButton = findViewById<Button>(R.id.addFirebaseDataButton)
        val getFirebaseDataButton = findViewById<Button>(R.id.getFirebaseDataButton)
        val raindropsDateText = findViewById<TextView>(R.id.raindropsDateText)
        val raindropsStatusText = findViewById<TextView>(R.id.raindropsStatusText)

        addFirebaseDataButton.setOnClickListener {
            firestore.collection("raindrops")
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

        getFirebaseDataButton.setOnClickListener {
            firestore.collection("raindrops")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("firebase", document.data["createdAt"].toString())
                        val date = document.data["createdAt"] as Timestamp
                        val formattedDate =
                            SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                        raindropsDateText.text = formattedDate.format(date.toDate())
                        raindropsStatusText.text = document.data["status"].toString()
                    }
                }
                .addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                    Snackbar.make(rootLayout, "Failure getting data", Snackbar.LENGTH_LONG).show()
                }
        }
    }
}