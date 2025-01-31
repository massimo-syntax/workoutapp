package com.example.workoutapp.data

import com.example.workoutapp.R

class ModelFake( type:String ){

    private var type:String = ""

    init {
        this.type =type
    }


    private val exercises : List<Exercise> = listOf(
        Exercise(0,"Arm","Lift handle using one arm, than the other",2, R.drawable.arm,false),
        Exercise(1,"Bike","Byke for the given time",3, R.drawable.byke,false),
        Exercise(2,"Crunches","Do some crunches as fast as you can",7, R.drawable.crunches,false),
        Exercise(3,"Handle","Lift the handle with 2 arms",3, R.drawable.handle,false),
        Exercise(4,"Jump","Jump around or on place",4, R.drawable.jump,false),
        Exercise(5,"Push up","On the floor belly down do some push ups",2, R.drawable.pushup,false),
        Exercise(6,"Run","Run following a path of your choice",4, R.drawable.run,false),
        Exercise(7,"Say No","Sometimes you can say no, however better to say yes more..",3, R.drawable.sayno,false),
        Exercise(8,"Squat","Take the chair away, do as if sitting, then stand up again, repeating..",5, R.drawable.squat,false),
        Exercise(9,"Wu Shu","Take stuff from your home and crash them using head, hands feet, or the mind",3, R.drawable.wushu,false),
    )

    fun getExercises():List<Exercise>{
        return exercises
    }

    val getType:String
        get()=type

    var setType = fun (s:String){this.type = s}



}