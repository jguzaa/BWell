package com.jguzaa.bwell.fragments.createHabit

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.HabitsType
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.util.APP_NAME
import com.jguzaa.bwell.util.ID
import com.jguzaa.bwell.util.setAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateHabitViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val app = application

    //Live data while database is loading
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    companion object {
        private const val TAG = "CreateHabitViewModel"
        private const val HOUR = "setHour"
        private const val MINUTE = "setMinute"
    }

    //Habit properties
    private var setHour: Int = 0
    private var setMinute: Int = 0
    var habit = Habit()


    //pref for recovery data after reboot
    private var prefs = app.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

    init {
        _dataLoading.value = false
    }

    fun start(){
        viewModelScope.launch {
            _dataLoading.value = true
            habit.habitId = getLastHabitId() + 1
            _dataLoading.value = false
        }
    }

    fun createHabit() {

        Log.d(TAG, "Clicked, Hour = $setHour, Min = $setMinute")
        Log.d(TAG, "Clicked, Habit name = ${habit.name}")

        //set notification time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, setHour)
        calendar.set(Calendar.MINUTE, setMinute)
        calendar.set(Calendar.SECOND, 0)

        habit.notificationTime = calendar.timeInMillis

        viewModelScope.launch {
            saveHabit(habit)
        }
        addHabit()
        setAlarm(habit.habitId, habit.name, app, habit.notificationTime)

    }

    //===============Database binding==================
    private fun addHabit() {
        viewModelScope.launch{
            database.insert(habit)
        }
    }

    private suspend fun getLastHabitId(): Long {
        return if (database.getLastHabit() == null) 0L
        else database.getLastHabit()!!.habitId
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

    // TODO: need to update
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
                //setAlarm()
            }
        }
    }

}