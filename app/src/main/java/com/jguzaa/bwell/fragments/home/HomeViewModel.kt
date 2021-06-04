package com.jguzaa.bwell.fragments.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import kotlinx.coroutines.launch

class HomeViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val TAG = "HomeViewModel"

    val nights = database.getAllHabits()

    fun addHabit(){
        Log.d(TAG, "fab click from Home ViewModel")
    }

    fun onClear(){
        viewModelScope.launch{
            database.clear()
        }
    }

}