package com.jguzaa.bwell.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jguzaa.bwell.data.local.HabitDatabaseDao

class HomeViewModel(
    val database: HabitDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG= "HomeViewModel"
    }

    val habits = database.getAllHabitsLiveData()

    private val _navigateToHabitDetail = MutableLiveData<Long>()
    val navigateToHabitDetail get() = _navigateToHabitDetail

    fun onHabitClicked(id: Long) {
        _navigateToHabitDetail.value = id
    }

    fun resetNavigate() {
        _navigateToHabitDetail.value = null
    }

}