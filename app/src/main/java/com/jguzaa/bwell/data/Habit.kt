package com.jguzaa.bwell.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class Habit(

    @PrimaryKey(autoGenerate = true)
    var habitId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "notification_time")
    var notificationTime: Long = 0L,

    @ColumnInfo(name = "today_finished")
    var todayFinished: Boolean = false,

    @ColumnInfo(name = "streak")
    var streak: Int = 0,

    @ColumnInfo(name = "is_snoozed")
    var isSnoozed: Boolean = false,

    @ColumnInfo(name = "last_day_finished")
    var lastDayFinished: Long = 0L,

    @ColumnInfo(name = "finish_percentage")
    var finishPercentages: Int = 0,

    @ColumnInfo(name = "type")
    var type: HabitsType = HabitsType.DAILY_TRACKING
){
    //setter and getter

}