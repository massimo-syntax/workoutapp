package com.example.workoutapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Query("SELECT * FROM exercise_table ORDER BY id ASC")
    fun getOrderedExercises(): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exer: Exercise)

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAll()
}
