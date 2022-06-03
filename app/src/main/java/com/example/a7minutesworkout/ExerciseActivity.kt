package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    // 1 create a binding variable
    private var binding: ActivityExcerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //2 inflate the layout
        binding = ActivityExcerciseBinding.inflate(layoutInflater)

        // 3 pass in binding?.root in the content view
        setContentView(binding?.root)

        // 4: then set support action bar and get toolBarExerciser using the binding
        //variable

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    private fun setupRestView() {
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = "${10 - restProgress} "
            }

            override fun onFinish() {
                currentExercisePosition++
                setUpExerciseView()
            }
        }.start()
    }

    private fun setUpExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].image)
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].name
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        binding?.exerciseProgressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = 30 - exerciseProgress
                binding?.tvExerciseTimer?.text = "${30 - exerciseProgress} "
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList!!.size - 1) {
                    setupRestView()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        binding = null
    }
}