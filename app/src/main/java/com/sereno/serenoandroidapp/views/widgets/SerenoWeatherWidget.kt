package com.sereno.serenoandroidapp.views.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sereno.serenoandroidapp.BuildConfig
import com.sereno.serenoandroidapp.R
import com.sereno.serenoandroidapp.services.OpenWeatherMapService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread


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
    val pref = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val firestore = FirebaseFirestore.getInstance()
    val formattedDate = SimpleDateFormat("yyy/MM/dd HH:mm", Locale.getDefault())

    // 3分ごとに取得
    Timer().schedule(1000, 180000) {
        if (pref.getString("cityName", "")!!.isNotEmpty()) {
            views.setTextViewText(R.id.widgetCurrentCityName, pref.getString("cityName", ""))
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val cityName = pref.getString("cityName", "").toString()

        firestore.collection("raindrops")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("firebase", document.data["createdAt"].toString())
                    val date = document.data["createdAt"] as Timestamp
                    val weatherStatus = document.data["status"] as Boolean

                    if (weatherStatus) {
                        views.setInt(
                            R.id.widgetBackground,
                            "setBackgroundResource",
                            R.drawable.background_style_rain
                        )
                    } else {
                        views.setInt(
                            R.id.widgetBackground,
                            "setBackgroundResource",
                            R.drawable.background_style_sunny
                        )
                    }

                    views.setTextViewText(
                        R.id.widgetDeviceUpdateDateText,
                        formattedDate.format(date.toDate())
                    )
                }
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

        thread {
            try {
                val service: OpenWeatherMapService =
                    retrofit.create(OpenWeatherMapService::class.java)
                val weatherApiResponse = service.getCurrentWeatherData(
                    cityName, BuildConfig.OWM_API_KEY, "metric", "ja"
                ).execute().body()
                    ?: throw IllegalStateException("body is null")

                val currentTime = weatherApiResponse.dt.toLong() * 1000
                val currentTemp = weatherApiResponse.main.temp.toString()

                Log.d("city-name", cityName)
                Log.d("time", formattedDate.format(currentTime))

                Handler(Looper.getMainLooper()).post {
                    views.setTextViewText(
                        R.id.widgetTempUpdateDateText,
                        formattedDate.format(currentTime)
                    )
                    views.setTextViewText(R.id.widgetTempText, currentTemp)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                Log.d("response-error", "debug $e")
            }
        }
    }
}