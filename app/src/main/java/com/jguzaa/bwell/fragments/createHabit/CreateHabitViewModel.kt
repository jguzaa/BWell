package com.jguzaa.bwell.fragments.createHabit

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.receiver.AlarmReceiver
import com.jguzaa.bwell.util.cancelNotifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateHabitViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val app = application

    companion object {
        private const val TAG = "CreateHabitViewModel"
        private const val HOUR = "setHour"
        private const val MINUTE = "setMinute"
        private const val REQUEST_CODE = 0
        private const val APP_NAME = "com.jguzaa.bwell"
        private const val ID = "HabitId"
    }

    //========Notification implement
    private var notifyPendingIntent: PendingIntent

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notifyIntent = Intent(app, AlarmReceiver::class.java)

    //Habit properties
    private var setHour: Int = 0
    private var setMinute: Int = 0
    private var triggerTime = 0L
    var habit = Habit()


    //pref for recovery data after reboot
    private var prefs = app.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

    init {

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    private fun setAlarm(){
        Log.d(TAG, "setAlarm, id from dao = ${habit.habitId}")
        //set the data to pending intent
        notifyIntent.putExtra(ID,habit.habitId)
        notifyIntent.action = APP_NAME

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Alarm manager for pending intent to run in the background
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            notifyPendingIntent
        )
    }

    fun start(){
        viewModelScope.launch {
            habit.habitId = getLastHabitId() + 1
        }
    }

    fun createHabit() {

        Log.d(TAG, "Clicked, Hour = $setHour, Min = $setMinute")
        Log.d(TAG, "Clicked, Habit name = ${habit.name}")

        // cancel old notification before start the new one
        val notificationManager = ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()

        //set trigger time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, setHour)
        calendar.set(Calendar.MINUTE, setMinute)
        calendar.set(Calendar.SECOND, 0)

        triggerTime = calendar.timeInMillis

        habit.notificationTime = triggerTime

        viewModelScope.launch {
            saveHabit(habit)
        }
        addHabit()
        setAlarm()

    }

    //===============Database binding==================
    private fun addHabit() {
        viewModelScope.launch{
            database.insert(habit)
        }
    }

    private suspend fun getLastHabitId(): Long {
        return if (database.getLastHabit() == null)
            0L
        else
            database.getLastHabit()!!.habitId

    }

    fun onClear(){
        viewModelScope.launch{
            clear()
        }
    }

    private suspend fun clear() {
        database.clear()
    }


    //====================Misc======================
    fun updateCurrentTime(hour:Int, minute:Int){
        setHour = hour
        setMinute = minute
    }

    private suspend fun saveHabit(habitAdd: Habit) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(ID, habitAdd.habitId).apply()
            prefs.edit().putInt(HOUR, setHour).apply()
            prefs.edit().putInt(MINUTE, setMinute).apply()
        }

    fun loadTime() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                habit.habitId = prefs.getLong(ID, 0L)
                setHour = prefs.getInt(HOUR, 0)
                setMinute = prefs.getInt(MINUTE, 0)
                Log.d(TAG, "Load, habit id = ${habit.habitId}")
                setAlarm()
            }
        }
    }

}