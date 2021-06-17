package com.jguzaa.bwell.receiver

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.local.HabitDatabase
import com.jguzaa.bwell.fragments.createHabit.CreateHabitViewModel
import com.jguzaa.bwell.util.sendNotification
import kotlinx.coroutines.launch

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
        private const val REQUEST_CODE = 0
        private const val APP_NAME = "com.jguzaa.bwell"
        private const val ID = "HabitId"
    }

    override fun onReceive(context: Context, intent: Intent?) {

        if(intent!!.action.equals(APP_NAME)){
            val id = intent.getLongExtra(ID,0L)

            Log.d(TAG, "id received = $id")

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            //Call notification
            notificationManager.sendNotification(
                context.getString(R.string.notification_msg),
                context,
                id
            )
        } else if (intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            //restore time after rebooted
            val application = context.applicationContext as Application
            val dataSource = HabitDatabase.getInstance(application).habitDatabaseDao
            val createHabitViewModel = CreateHabitViewModel(dataSource, application)

            Log.d(TAG, "Load alarm after rebooted")

            createHabitViewModel.loadTime()
        }

    }


}