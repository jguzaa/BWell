package com.jguzaa.bwell.fragments.createHabit

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.HabitsType
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.util.APP_NAME
import com.jguzaa.bwell.util.setAlarm
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
    }

    //Habit properties
    private var setHour: Int = 0
    private var setMinute: Int = 0
    var habit = Habit()

    fun createHabit() {

        Log.d(TAG, "Clicked, Hour = $setHour, Min = $setMinute")
        Log.d(TAG, "Clicked, Habit name = ${habit.name}")

        //set notification time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, setHour)
        calendar.set(Calendar.MINUTE, setMinute)
        calendar.set(Calendar.SECOND, 0)

        habit.notificationTime = calendar.timeInMillis

        addHabit()

    }

    //===============Database process==================
    private fun addHabit() {
        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                habit.habitId = database.insert(habit)
                setAlarm(habit.habitId, habit.name, app, habit.notificationTime)
            }
        }
    }

    //====================Misc======================
    fun updateCurrentTime(hour:Int, minute:Int){
        setHour = hour
        setMinute = minute
    }

    fun setTimeSelected(selection: Int) {
        when (selection){
            0 -> habit.type = HabitsType.DAILY_TRACKING
            1 -> habit.type = HabitsType.ALARM_CLOCK
            2 -> habit.type = HabitsType.JOGGING
        }
        Log.d(TAG, "habit type = ${habit.type}")
    }

    fun loadTime() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val habits = database.getAllHabits()
                for(habit in habits){
                    setAlarm(habit.habitId, habit.name, app, habit.notificationTime)
                }
            }
        }
    }

}