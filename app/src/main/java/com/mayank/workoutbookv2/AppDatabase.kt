package com.mayank.workoutbookv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkoutEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDAO(): WorkoutDAO

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase?=null

        fun getDatabase(context:Context):AppDatabase{
            synchronized(this){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "WorkoutDatabase"
                    ).build()
                }
            }


            return INSTANCE!!
        }
    }
}