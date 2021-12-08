package com.sereno.serenoandroidapp.views.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sereno.serenoandroidapp.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class SerenoWeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget_sereno_weather)
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    val rtdb = Firebase.database.reference

    rtdb.child("raindrops/").get().addOnSuccessListener {
        val weatherStatus = it.child("status").value
        if (weatherStatus == true) {
            views.setInt(R.id.widgetBackground, "setBackgroundResource",R.drawable.image_rain)
        } else {
            views.setInt(R.id.widgetBackground, "setBackgroundResource",R.drawable.image_sunny)
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
        Log.d("response-status", it.child("status").value.toString())
    }.addOnFailureListener {
        Log.e("firebase", "Error getting data", it)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}