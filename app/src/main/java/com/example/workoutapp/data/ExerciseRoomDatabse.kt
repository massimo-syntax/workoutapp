package com.example.workoutapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Exercise::class), version = 1, exportSchema = false)
public abstract class ExerciseRoomDatabse : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ExerciseRoomDatabse? = null

        fun getDatabase(context: Context): ExerciseRoomDatabse {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseRoomDatabse::class.java,
                    "exercise_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
