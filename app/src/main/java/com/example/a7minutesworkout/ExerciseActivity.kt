package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    // 1 create a binding variable
    private var binding:ActivityExcerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var totalDuration: Long = 10_000
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //2 inflate the layout
        binding = ActivityExcerciseBinding.inflate(layoutInflater)

        // 3 pass in binding?.root in the content view
        setContentView(binding?.root)

        // 4: then set support action bar and get toolBarExerciser using the binding
        //variable

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpView()
    }

    private fun setUpView(){
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(totalDuration,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = "${millisUntilFinished / 1000} "
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, "Here we start the exercise", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding = null
    }
}