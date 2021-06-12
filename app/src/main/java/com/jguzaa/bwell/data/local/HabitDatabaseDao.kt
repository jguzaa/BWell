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
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Query("SELECT * from habit_table WHERE habitId = :key")
    suspend fun get(key: Long): Habit?

    @Query("DELETE FROM habit_table")
    suspend fun clear()

    //select and return all habits, sort by id
    @Query("SELECT * FROM habit_table ORDER BY habitId")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun getHabitWithId(key: Long): LiveData<Habit>

    @Query("SELECT * FROM habit_table ORDER BY habitId DESC LIMIT 1")
    suspend fun getLastHabit(): Habit?

    @Query("SELECT * from habit_table WHERE habitId = :key")
    suspend fun getHabitWithIdNonLiveReturn(key: Long): Habit?
}