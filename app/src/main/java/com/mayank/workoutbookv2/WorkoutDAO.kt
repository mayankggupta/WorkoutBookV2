package com.mayank.workoutbookv2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM workoutbook WHERE Workday = 1")
    fun getMon(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workoutbook WHERE Workday = 2")
    fun getTue(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workoutbook WHERE Workday = 3")
    fun getWed(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workoutbook WHERE Workday = 4")
    fun getThu(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workoutbook WHERE Workday = 5")
    fun getFri(): Flow<List<WorkoutEntity>>
    @Upsert
    suspend fun insertAll(vararg workouts: WorkoutEntity)


    @Delete
    suspend fun delete(workout: WorkoutEntity)
}