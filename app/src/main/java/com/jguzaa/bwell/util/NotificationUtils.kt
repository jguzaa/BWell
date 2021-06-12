package com.jguzaa.bwell.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.jguzaa.bwell.MainActivity
import com.jguzaa.bwell.R
import com.jguzaa.bwell.fragments.AccountFragment


private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0
private const val TAG = "NotificationUtils"

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    Log.d(TAG, "getNotification triggered")

    //Create intent when notification pressed
//    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    //Create pending intent

    //Navigate to main activity
//    val contentPendingIntent = PendingIntent.getActivity(
//        applicationContext,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )

    //Navigate to createHabitFragment
    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.createHabitFragment)
        .createPendingIntent()


    //add logo
    val logoImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.logo250
    )

    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(logoImage)
        .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.habit_notification_channel_id)
    )
        .setSmallIcon(R.drawable.logo250)
        .setContentTitle(applicationContext.getString(R.string.app_name))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPictureStyle)
        .setLargeIcon(logoImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}