package com.jguzaa.bwell.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jguzaa.bwell.data.Habit

@Dao
interface HabitDatabaseDao {

    @Insert
    suspend fun insert(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIgnorePrimaryKey(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Query("DELETE FROM habit_table")
    suspend fun clear()

    //select and return all habits, sort by id
    @Query("SELECT * FROM habit_table ORDER BY habitId")
    fun getAllHabitsLiveData(): LiveData<List<Habit>>

    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun getHabitWithId(key: Long): LiveData<Habit>

    @Query("SELECT * from habit_table WHERE habitId = :key")
    fun getHabit(key: Long): Habit?

    @Query("SELECT * FROM habit_table ORDER BY habitId")
    fun getAllHabits(): List<Habit>

    @Delete
    fun deleteHabit(habit: Habit)
}