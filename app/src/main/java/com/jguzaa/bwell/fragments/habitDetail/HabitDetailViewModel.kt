package com.jguzaa.bwell.fragments.habitDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.HABIT_DATE
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.util.cancelAlarm
import com.jguzaa.bwell.util.setAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HabitDetailViewModel(
    val database: HabitDatabaseDao,
    application: Application,
    val habitId: Long = 0L
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "HabitDetailVM"
    }

    private val app = application
    private val _habit = MediatorLiveData<Habit>()
    val habit get() = _habit

    init {
        _habit.addSource(database.getHabitWithId(habitId), _habit::setValue)
    }

    fun todayFinish(){
        val habitTemp = _habit.value

        habitTemp!!.streak++
        habitTemp.todayFinished = true
        habitTemp.isSnoozed = false

        val today = Calendar.getInstance().timeInMillis
        habitTemp.lastDayFinished = today

        habitTemp.finishPercentages = 100 * habitTemp.streak / HABIT_DATE

        updateHabit(habitTemp)
    }

    private fun updateHabit(habit: Habit) {
        _habit.value = habit
        viewModelScope.launch{
            database.update(habit)
        }
    }

    fun dailyReset() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val habitTemp = database.getHabit(habitId)
                habitTemp!!.todayFinished = false
                habitTemp.isSnoozed = false
                Log.d(TAG, "daily reset for id ${habitTemp.habitId}")
                database.update(habitTemp)
            }
        }
    }

    fun snoozeAndResetStreak() {
        val habitTemp = _habit.value
        habitTemp!!.streak = 0
        habitTemp.isSnoozed = true
        updateHabit(habitTemp)
    }

    fun snooze(){
        val habitTemp = _habit.value
        habitTemp!!.isSnoozed = true
        updateHabit(habitTemp)
    }

    fun updateNotificationTime(hour: Int, minute: Int) {

        val habitTemp = _habit.value

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        habitTemp!!.notificationTime = calendar.timeInMillis

        //set new notification time
        setAlarm(habitTemp.habitId, habitTemp.name, app, habitTemp.notificationTime)

        //update db
        updateHabit(habitTemp)

    }

    fun deleteHabit() {

        cancelAlarm(_habit.value!!.habitId, app)

        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                database.deleteHabit(_habit.value!!)
            }
        }
    }

}