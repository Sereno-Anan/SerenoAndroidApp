package com.sereno.serenoandroidapp.views.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sereno.serenoandroidapp.R
import java.util.*
import kotlin.concurrent.schedule

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

    // 3分ごとに取得
    Timer().schedule(1000, 180000) {
        rtdb.child("raindrops/").get().addOnSuccessListener {
            val weatherStatus = it.child("status").value

            if (weatherStatus == true) {
                views.setInt(R.id.widgetBackground, "setBackgroundResource", R.drawable.background_style_rain)
            } else {
                views.setInt(R.id.widgetBackground, "setBackgroundResource", R.drawable.background_style_sunny)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d("response-status", it.child("status").value.toString())
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}