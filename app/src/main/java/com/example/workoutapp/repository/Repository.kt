package com.example.workoutapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.workoutapp.data.Dao
import com.example.workoutapp.data.Exercise


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class Repository(private val dao: Dao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allExercises: LiveData<List<Exercise>> = dao.getOrderedExercises()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Exercise) {
        dao.insert(word)
    }

    suspend fun flush(){
        dao.deleteAll()
    }
}
