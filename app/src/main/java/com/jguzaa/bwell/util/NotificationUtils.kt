package com.jguzaa.bwell.util

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.jguzaa.bwell.MainActivity
import com.jguzaa.bwell.R

private const val TAG = "NotificationUtils"

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, id: Long) {

    Log.d(TAG, "getNotification triggered")

    //Navigate to createHabitFragment
    val args = Bundle()
    args.putLong("habitId", id)

    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.habitDetailFragment)
        .setArguments(args)
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

    notify(id.toInt(), builder.build())
}