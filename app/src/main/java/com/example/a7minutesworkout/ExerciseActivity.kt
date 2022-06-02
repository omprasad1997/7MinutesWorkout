package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    // 1 create a binding variable
    private var binding:ActivityExcerciseBinding? = null

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

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}