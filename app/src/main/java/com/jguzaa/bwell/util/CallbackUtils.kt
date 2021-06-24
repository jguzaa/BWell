package com.jguzaa.bwell.util

import com.jguzaa.bwell.data.Habit

const val FAIL_CODE = 0
const val SUCCESS_CODE = 1
const val LOADING_CODE = 2

//TODO : Adapt for Habit list callback status
interface HabitsCallback {
    fun onCallback(value: Result<List<Habit>>)
}

interface StatusCallback {
    fun onCallback(status: Int)
}