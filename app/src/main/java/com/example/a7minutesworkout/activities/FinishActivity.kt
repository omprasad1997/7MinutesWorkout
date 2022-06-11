package com.example.a7minutesworkout.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.database.HistoryDao
import com.example.a7minutesworkout.WorkoutApp
import com.example.a7minutesworkout.database.HistoryEntity
import com.example.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding:ActivityFinishBinding? = null
    private var player : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            player?.stop()
            finish()
        }

        playWorkoutFinishedMusic()

        val dao = (application as WorkoutApp).db.historyDao()
        addDateToDatabase(dao)
    }

    private fun playWorkoutFinishedMusic() {

        try {
            player = MediaPlayer.create(applicationContext, R.raw.finished_workout)
            player?.isLooping = false
            player?.start()

        } catch (e : Exception){
            e.printStackTrace()
        }

    }

    private fun addDateToDatabase(historyDao: HistoryDao){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.e("Date",""+dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("FormattedDate",""+date)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("Date:","Added...")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(player != null){
            player = null
        }

        binding = null
    }
}















