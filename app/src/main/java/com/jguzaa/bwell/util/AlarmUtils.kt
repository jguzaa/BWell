package com.jguzaa.bwell.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jguzaa.bwell.receiver.AlarmReceiver

const val APP_NAME = "com.jguzaa.bwell"
const val ID = "HabitId"
const val HABIT_NAME = "HabitName"

fun setAlarm(habitId: Long, habitName: String, app: Application, time: Long){

    val notifyIntent = Intent(app, AlarmReceiver::class.java)
    val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    notifyIntent.putExtra(ID,habitId)
    notifyIntent.putExtra(HABIT_NAME,habitName)
    notifyIntent.action = APP_NAME

    val notifyPendingIntent = PendingIntent.getBroadcast(
        app,
        habitId.toInt(), //for different requestCode
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //Alarm manager for pending intent to run in the background
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        time,
        AlarmManager.INTERVAL_DAY,
        notifyPendingIntent
    )

    Log.d("AlarmUtils","Alarm for id $habitId is set")
}

fun cancelAlarm(habitId: Long, app: Application){

    val notifyIntent = Intent(app, AlarmReceiver::class.java)
    val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notifyPendingIntent = PendingIntent.getBroadcast(
        app,
        habitId.toInt(), //for different requestCode
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    notifyPendingIntent.cancel()
    alarmManager.cancel(notifyPendingIntent)

    Log.d("AlarmUtils","Alarm for id $habitId is canceled")
}
