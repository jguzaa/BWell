package com.jguzaa.bwell.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.jguzaa.bwell.R
import com.jguzaa.bwell.util.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {

        Log.d(TAG, "Alarm received")

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        //Call notification
        notificationManager.sendNotification(
            context.getString(R.string.notification_msg),
            context
        )
    }


}