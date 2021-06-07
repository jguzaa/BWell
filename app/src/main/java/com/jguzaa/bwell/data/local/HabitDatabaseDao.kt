package com.jguzaa.bwell.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jguzaa.bwell.data.Habit

@Dao
interface HabitDatabaseDao {

    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Query("SELECT * from habit_table WHERE habitId = :key")
    suspend fun get(key: Long): Habit?

    @Query("DELETE FROM habit_table")
    suspend fun clear()

    //select and return all habits, sort by id
    @Query("SELECT * FROM habit_table ORDER BY habitId")
    fun getAllHabits(): LiveData<List<Habit>>

    //select and return the livedata habit
    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun getHabitWithId(key: Long): LiveData<Habit>
}