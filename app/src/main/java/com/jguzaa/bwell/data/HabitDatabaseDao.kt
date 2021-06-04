package com.jguzaa.bwell.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDatabaseDao {

    @Insert
    fun insert(habit: Habit)

    @Update
    fun update(habit: Habit)

    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun get(key: Long): Habit?

    @Query("DELETE FROM habit_table")
    fun clear()

    //select and return all habits, sort by id
    @Query("SELECT * FROM habit_table ORDER BY habitId")
    fun getAllHabits(): LiveData<List<Habit>>

    //select and return the livedata habit
    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun getHabitWithId(key: Long): LiveData<Habit>
}