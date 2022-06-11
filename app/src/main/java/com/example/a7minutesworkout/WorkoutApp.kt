package com.example.a7minutesworkout

import android.app.Application
import com.example.a7minutesworkout.database.HistoryDatabase

class WorkoutApp : Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}