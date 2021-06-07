package com.jguzaa.bwell.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jguzaa.bwell.data.local.HabitDatabaseDao

class HomeViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG= "HomeViewModel"
    }

    val habits = database.getAllHabits()

}