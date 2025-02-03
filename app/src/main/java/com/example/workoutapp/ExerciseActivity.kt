package com.example.workoutapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.workoutapp.adapter.ExerciseAdapter
import com.example.workoutapp.data.Exercise
import com.example.workoutapp.data.ExerciseRoomDatabse
import com.example.workoutapp.data.ModelFake
import com.example.workoutapp.databinding.ActivityExcerciseBinding
import com.example.workoutapp.repository.Repository
import com.example.workoutapp.viewmodel.ExerciseViewModel
import com.example.workoutapp.viewmodel.ExerciseViewModelFactory


class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExcerciseBinding
    private lateinit var time : CountDownTimer
    private lateinit var adapter: ExerciseAdapter
    private lateinit var rv : RecyclerView
    private var remainingTime : Long = 0


    private val ONE_SECOND : Long = 1000
    //private lateinit var mediaPlayer: MediaPlayer

    private val modelFake:ModelFake = ModelFake("singleton")
    private var exercises:List<Exercise> = modelFake.getExercises()

     private var exerciseNumber : Int = 0


    private fun setExerciseNumber(n:Int){
        this.exerciseNumber = n
    }

    private fun getExerciseNumber() : Int{
        return this.exerciseNumber
    }


    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ExerciseRoomDatabse.getDatabase(this) }
    val repository by lazy { Repository(database.dao()) }


    //private val viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
    //private val viewModel: ExerciseViewModel = ExerciseViewModel(repository)

    private val viewModel: ExerciseViewModel by viewModels {
        ExerciseViewModelFactory(repository)
    }


    object dashboard {
        lateinit var time : CountDownTimer
        var start : ()->Unit = fun (){}
        var updateExerciseNumber : (n:Int)->Unit = fun (n:Int){}
        var getExerciseNumber : () -> Int = fun(): Int {return 0}
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.flush()
        var exr:String = ""
        viewModel.allExercises.observe(this, Observer { exercises ->
            // Update the cached copy of the words in the adapter.
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].title)
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].description)
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].title)
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].title)
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].title)
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].time.toString())
            Log.wtf("DFLAKFJLASDKFJALF", exercises[0].description)
        })
        viewModel.insert(exercises[3])
        viewModel.insert(exercises[4])

        // init media player
        //mediaPlayer = MediaPlayer.create(this, R.raw.beep)

        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BUTTON FOR CUSTOM TOOLBAR
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // recycler view
        rv = binding.rvExcercises
        rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        dashboard.start = fun(){ start() } // or settimeto rest
        dashboard.updateExerciseNumber = fun(n:Int){ this.setExerciseNumber(n) }
        //dashboard.getExerciseNumber = fun():Int{return this.getExerciseNumber()}

        adapter = ExerciseAdapter(exercises , dashboard)
        rv.adapter = adapter


        // set a welcome time of 3, (3 before the 00, 00 lasts the last second) then start
        binding.title.text = "Ready ?"
        setTimerinSeconds(3 + 1 , {dashboard.start()})
                                /* 1st tick takes 1 away to display  */

    }// onCreate

    private fun start(){
        // first the app begins with setTimeToRest(), just to take air..
        exercises.forEachIndexed { index, exercise ->
            if(exercise.selected){
                exercise.selected = false
                adapter.notifyItemChanged(index)
            }
        }

        loadExcercise()
        /* SET TIME TO REST BEGINS TO COUNT, AT THE END STARTS THE CALLBACK CIRLCE */
    }


    private fun rvScroll(){
        // is scroling to the end, not moving back the begin (or the end is space)
        // with 5 elements, 4 visible in the screen, scrolling to the 5th hides the 1st, so the 2nd is the 1st
        val howMany = 2
        if( exerciseNumber < 2 )return
        if( exerciseNumber + howMany <= exercises.size -1 ) rv.smoothScrollToPosition(exerciseNumber+howMany)
    }



    private fun end(){
        binding.title.text = "finished"
        binding.progressBar.visibility = View.INVISIBLE
        // hide description to avoid scaring the user
        binding.description.visibility = View.INVISIBLE
        binding.ivImage.visibility = View.INVISIBLE
        binding.tvTimer.text="End"

        // get recyclerview away
        ObjectAnimator.ofFloat(rv,"translationX",-1000f).apply {
            duration = 1000
            start() // ;)
        }

        val frame : FrameLayout = binding.fvCounterBorder

        val animScaleX = ObjectAnimator.ofFloat(frame, "scaleX", 1000f)
        val animScaleY = ObjectAnimator.ofFloat(frame, "scaleY", 1000f)
        val alpha = ObjectAnimator.ofFloat(frame, "alpha",0f)
        val position = ObjectAnimator.ofFloat(frame,"translationY",-5000f)
        val positionTitle = ObjectAnimator.ofFloat(binding.title,"translationY",150f)

        AnimatorSet().apply {
            playTogether(animScaleX, animScaleY, alpha, position, positionTitle)
            duration = 2000
            start()
        }

    }


    private fun setTimeToRest(){
        if(exerciseNumber >= exercises.size) {end();return}

        // let the user rest knowing it
        binding.progressBar.visibility = View.VISIBLE
        // hide description to avoid scaring the user
        binding.description.visibility = View.INVISIBLE
        binding.ivImage.visibility = View.INVISIBLE

        binding.title.text = "Take some rest.."
        binding.title.translationY = 0f

        // get views back on position
        binding.title.translationY = 0f
        binding.description.translationY = 0f
        ObjectAnimator.ofFloat(binding.fvCounterBorder,"translationY",0f).apply {
            duration = 1000
            start() // ;)
        }

        setTimerinSeconds(/*5 +*/ 1 , { loadExcercise() })
                                /* 1st tick takes 1 away to display  */
    }

    private fun loadExcercise(){
        // just in case
        if(exerciseNumber >= exercises.size){end();return}
        // scroll to next
        rvScroll()

        // make the current element very chich
        if( exerciseNumber > 0 ){
            exercises[exerciseNumber-1].selected = false
            adapter.notifyItemChanged(exerciseNumber-1)
        }
        exercises[exerciseNumber].selected = true
        adapter.notifyItemChanged(exerciseNumber)


        // hide progress bar to prevent stress to user while excercising
        binding.progressBar.visibility = View.INVISIBLE
        // show description
        binding.description.visibility = View.VISIBLE
        binding.ivImage.visibility = View.VISIBLE

        // load data
        setExerciseToViews()

        // setup screen to have image , description, all needed in the center..
        binding.title.translationY = -250f
        binding.description.translationY = -250f
        ObjectAnimator.ofFloat(binding.fvCounterBorder,"translationY",250f).apply {
            duration = 1000
            start()
        }
        // get time for current excercise, than update exercise number
        val timerThisExcercise = exercises[exerciseNumber].time.toLong()
        exerciseNumber++
        // set timer for this excercise, onFinish() of timer call time to rest
        setTimerinSeconds(/*timerThisExcercise + */1 ,{setTimeToRest()})
                                                /*  1st tick takes 1 away to display  */
    }




    //  PRINT DATA INTO VIEWS
    fun setExerciseToViews(){
        binding.title.text = exercises[exerciseNumber].title
        binding.description.text = exercises[exerciseNumber].description
        binding.ivImage.setImageResource(exercises[exerciseNumber].image)
    }


    // TIMER
    private fun setTimerinSeconds(sec:Long, callback: () -> Unit ){
        var t : Long = 0 // ..ime
        remainingTime = 0
        // first tick takes already 1 second away, set remainingTime to -1000 does not work...
        dashboard.time = object : CountDownTimer(sec*ONE_SECOND , ONE_SECOND ) {
            override fun onTick(p0: Long) {
                // first print then update
                remainingTime += ONE_SECOND //
                t = 0
                t = (sec*ONE_SECOND - remainingTime)
                // now time is for example 10 000 - 1000 = 9000
                t /= 1000
                binding.progressBar.progress = t.toInt()    // set progress bar progressing
                binding.tvTimer.text = String.format("%02d", t)
            }
            override fun onFinish() {
                //Toast.makeText(this@ExerciseActivity,"finidhes",Toast.LENGTH_SHORT).show()
                callback()
            }
        }.start()
    }

}//this