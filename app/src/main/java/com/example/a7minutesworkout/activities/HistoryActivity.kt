package com.example.a7minutesworkout.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.adapter.HistoryAdapter
import com.example.a7minutesworkout.WorkoutApp
import com.example.a7minutesworkout.database.HistoryEntity
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.title = "History"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()

        lifecycleScope.launch {
           dao.fetchAllDates().collect {
               val list = ArrayList(it)
               setUpDatesIntoRecyclerView(list)
           }
        }
    }

    private fun setUpDatesIntoRecyclerView(list: ArrayList<HistoryEntity>) {

        if(list.isNotEmpty()){
            val itemAdapter = HistoryAdapter(list)
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = itemAdapter

            binding?.rvHistory?.visibility = View.VISIBLE
            binding?.tvHistory?.visibility = View.VISIBLE
            binding?.tvNoDataAvailable?.visibility  = View.GONE
        } else {
            binding?.rvHistory?.visibility = View.GONE
            binding?.tvHistory?.visibility = View.GONE
            binding?.tvNoDataAvailable?.visibility  = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}