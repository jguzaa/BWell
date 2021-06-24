package com.jguzaa.bwell.fragments.setting

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jguzaa.bwell.data.Habit
import com.jguzaa.bwell.data.local.HabitDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.jguzaa.bwell.data.Result.Success
import com.jguzaa.bwell.util.*


class SettingViewModel(
    val database: HabitDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser
    private val app = application
    private lateinit var habits: List<Habit>
    private val firebaseDatabase = FirebaseDatabase.getInstance().getReference(currentUser!!.uid)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    init{
        _isLoading.value = false
    }

    fun resetAll() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                habits = database.getAllHabits()

                //cancel all alarms
                for(habit in habits){
                    cancelAlarm(habit.habitId, app)
                }

                //reset database
                database.clear()
            }
        }
    }

    fun signOut(){
        mAuth.signOut()
    }

    fun backupToFirebase(statusCallback: StatusCallback) {

        _isLoading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                habits = database.getAllHabits()

                for(habit in habits){
                    firebaseDatabase.child(habit.habitId.toString()).setValue(habit)
                        .addOnSuccessListener {
                            Log.d(TAG, "Backing up : id = ${habit.habitId}")
                            statusCallback.onCallback(SUCCESS_CODE)
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "failed : $it")
                            statusCallback.onCallback(FAIL_CODE)
                            return@addOnFailureListener
                        }
                }
            }
            delay(1000)
            _isLoading.value = false

        }
    }

    fun restoreFromFirebase(statusCallback: StatusCallback) {

        _isLoading.value = true

        firebaseDatabase.get().addOnCompleteListener {
            if (it.isSuccessful) {

                val result = it.result
                result?.let {
                    val habitsFirebase = result.children.map { snapShot -> snapShot.getValue(Habit::class.java)!! }
                    viewModelScope.launch {
                        //save habits to local db and set notification
                        saveToLocalDb(habitsFirebase)
                        statusCallback.onCallback(SUCCESS_CODE)
                        delay(1000)
                        _isLoading.value = false
                    }
                }

            } else {
                statusCallback.onCallback(FAIL_CODE)
                _isLoading.value = false
            }
        }

    }

    private fun saveToLocalDb(habitsFirebase: List<Habit>) {

        viewModelScope.launch{
            withContext(Dispatchers.IO) {

                habits = database.getAllHabits()

                //cancel all alarms
                for(habit in habits){
                    cancelAlarm(habit.habitId, app)
                }

                //reset database
                database.clear()

                for(habit in habitsFirebase){
                    database.insertIgnorePrimaryKey(habit)
                    setAlarm(habit.habitId, habit.name, app, habit.notificationTime)
                }

            }
        }
    }

    companion object {
        private const val TAG = "SettingViewModel"
    }


}