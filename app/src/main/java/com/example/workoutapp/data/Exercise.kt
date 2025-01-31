package com.example.workoutapp.data

data class Exercise(
    val id:Long,
    val title:String,
    val description:String,
    val time: Int,
    val image:Int,
    var selected:Boolean
)
