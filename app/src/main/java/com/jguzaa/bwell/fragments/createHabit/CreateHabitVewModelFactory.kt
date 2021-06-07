package com.jguzaa.bwell.fragments.createHabit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import com.jguzaa.bwell.fragments.home.HomeViewModel

class CreateHabitVewModelFactory (
    private val dataSource: HabitDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateHabitViewModel::class.java)) {

            return CreateHabitViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}