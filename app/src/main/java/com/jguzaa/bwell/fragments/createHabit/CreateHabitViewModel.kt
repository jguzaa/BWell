package com.jguzaa.bwell.fragments.createHabit

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.R
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.receiver.AlarmReceiver
import com.jguzaa.bwell.util.cancelNotifications
import com.jguzaa.bwell.util.sendNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateHabitViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    val app = application

    companion object {
        private const val TAG = "CreateHabitViewModel"
    }

    //========Notification implement
    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val second: Long = 1_000L

    private val notifyPendingIntent: PendingIntent

    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private var prefs = app.getSharedPreferences("com.jguzaa.bwell", Context.MODE_PRIVATE)
    private val notifyIntent = Intent(app, AlarmReceiver::class.java)

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private var _alarmOn = MutableLiveData<Boolean>()
    val isAlarmOn: LiveData<Boolean>
        get() = _alarmOn


    private lateinit var timer: CountDownTimer

    init {
        _alarmOn.value = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //If alarm is not null, resume the timer back for this alarm
        if (_alarmOn.value!!) {
            createTimer()
        }

    }

    fun startTimer() {
        Log.d(TAG, "clicked")

        //Notification calling
//        val notificationManager = ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
//        notificationManager.sendNotification(app.getString(R.string.notification_msg), app)

        _alarmOn.value?.let {
            if (!it) {
                Log.d(TAG, "in alarm on")
                _alarmOn.value = true
                val selectedInterval = second * 2
                val triggerTime = SystemClock.elapsedRealtime() + selectedInterval

                // cancel old notification before start the new one
                val notificationManager = ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
                notificationManager.cancelNotifications()

                //Alarm manager for pending intent to run in the background
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent
                )

                viewModelScope.launch {
                    saveTime(triggerTime)
                }
            }
        }
        createTimer()
    }

    private fun createTimer() {
        viewModelScope.launch {
            val triggerTime = loadTime()
            timer = object : CountDownTimer(triggerTime, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value!! <= 0) {
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }

    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false
    }

    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }

    private suspend fun loadTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME, 0)
        }

    //===============Database binding
    fun addHabit(habit: Habit){
        viewModelScope.launch {
            insert(habit)
        }
    }

    fun onClear(){
        viewModelScope.launch{
            clear()
        }
    }

    private suspend fun insert(habit: Habit) {
        database.insert(habit)
    }

    private suspend fun clear() {
        database.clear()
    }
}