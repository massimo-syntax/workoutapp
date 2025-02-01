package com.example.workoutapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.repository.Repository
import kotlinx.coroutines.launch


class ExerciseViewModel(private val repository: Repository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allExercises: LiveData<List<Exercise>> = repository.allExercises

    /**
     *
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(exer: Exercise) = viewModelScope.launch {
        repository.insert(exer)
    }

    fun flush() = viewModelScope.launch { repository.flush() }


}

// no dagger hilt

class ExerciseViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



