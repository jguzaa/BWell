package com.jguzaa.bwell.fragments.habitDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.HABIT_DATE
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import kotlinx.coroutines.launch
import java.util.*

class HabitDetailViewModel(
    val database: HabitDatabaseDao,
    application: Application,
    habitId: Long = 0L
) : AndroidViewModel(application) {

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

        _habit.value = habitTemp

        updateHabit(habitTemp)
    }

    fun snooze(habit: Habit){
        updateHabit(habit)
    }


    private fun updateHabit(habit: Habit) {
        viewModelScope.launch{
            database.update(habit)
        }
    }

}