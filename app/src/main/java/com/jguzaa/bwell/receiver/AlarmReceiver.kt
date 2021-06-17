package com.jguzaa.bwell.receiver

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.fragments.createHabit.CreateHabitViewModel
import com.jguzaa.bwell.fragments.habitDetail.HabitDetailViewModel
import com.jguzaa.bwell.util.HABIT_NAME
import com.jguzaa.bwell.util.ID
import com.jguzaa.bwell.util.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
        private const val APP_NAME = "com.jguzaa.bwell"
    }

    override fun onReceive(context: Context, intent: Intent?) {

        val application = context.applicationContext as Application
        val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao

        if(intent!!.action.equals(APP_NAME)){
            val id = intent.getLongExtra(ID,0L)
            val name = intent.getStringExtra(HABIT_NAME)

            Log.d(TAG, "id received = $id")

            //reset daily tracking
            val habitDetailViewModel = HabitDetailViewModel(dataSource, application, id)
            habitDetailViewModel.dailyReset()

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            //Call notification
            notificationManager.sendNotification(
                context.getString(R.string.notification_msg, name),
                context,
                id
            )
        } else if (intent.action.equals("android.intent.action.BOOT_COMPLETED")){

            //TODO : fix after reboot
            //restore time after rebooted
            val createHabitViewModel = CreateHabitViewModel(dataSource, application)

            Log.d(TAG, "Load alarm after rebooted")

            createHabitViewModel.loadTime()
        }

    }


}