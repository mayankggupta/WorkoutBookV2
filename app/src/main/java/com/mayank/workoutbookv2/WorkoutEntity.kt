package com.mayank.workoutbookv2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workoutbook")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "WorkoutName") val WorkoutName: String?,
    @ColumnInfo(name = "Weight") val Weight: String?,
    @ColumnInfo(name="Workday") val Workday:Byte?
)