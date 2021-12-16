package com.sereno.serenoandroidapp.models.db.firestore

import com.google.firebase.firestore.FieldValue

data class RainDrops(
    val status: Boolean,
    val createdAt: FieldValue,
)
