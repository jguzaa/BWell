package com.jguzaa.bwell.fragments.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.util.cancelAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(
    val database: HabitDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val mAuth = FirebaseAuth.getInstance()
    private val app = application

    fun resetAll() {

        //sign out google
        mAuth.signOut()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val habits = database.getAllHabits()

                //cancel all alarms
                for(habit in habits){
                    cancelAlarm(habit.habitId, app)
                }

                //reset database
                database.clear()
            }
        }
    }

    companion object {
        private const val TAG = "SettingViewModel"
    }


}