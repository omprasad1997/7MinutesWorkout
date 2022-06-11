package com.example.a7minutesworkout.activities

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.Constants
import com.example.a7minutesworkout.adapter.HistoryAdapter
import com.example.a7minutesworkout.WorkoutApp
import com.example.a7minutesworkout.database.HistoryDao
import com.example.a7minutesworkout.database.HistoryEntity
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import com.example.a7minutesworkout.databinding.DialogClearAllExerciseDatesConfirmationBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    var binding: ActivityHistoryBinding? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Constants.getFullScreenActivity(window)
        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.title = "History"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()
        getAllCompletedWorkoutDates(dao)

        binding?.ivClearAllDates?.setOnClickListener {
            clearAllDatesRecordDialog(dao)
        }
    }

    private fun showProgressDialog() {
        dialog = ProgressDialog(this);
        dialog!!.setMessage("please wait...");
        dialog!!.show();
    }

    private fun hideProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    private fun clearAllDatesRecordDialog(dao: HistoryDao) {
        val customDialog = Dialog(this)
        val dialogBinding = DialogClearAllExerciseDatesConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            customDialog.dismiss()
            lifecycleScope.launch {
                dao.fetchAllDates().collect { listOfHistoryEntity ->
                    showProgressDialog()
                    if (listOfHistoryEntity.isNotEmpty()) {
                        for (historyEntity in listOfHistoryEntity) {
                            dao.deleteExerciseByDate(HistoryEntity(historyEntity.date))
                        }
                        Toast.makeText(
                            this@HistoryActivity,
                            "All Exercise deleted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        hideProgressDialog()
                    } else {
                        hideProgressDialog()
                    }
                }
            }
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun deleteCompletedDate(dao: HistoryDao, date: String) {
        lifecycleScope.launch {
            dao.deleteExerciseByDate(HistoryEntity(date))
        }
    }

    private fun getAllCompletedWorkoutDates(dao: HistoryDao) {

        lifecycleScope.launch {
            dao.fetchAllDates().collect {
                val list = ArrayList(it)
                setUpDatesIntoRecyclerView(dao, list)
            }
        }
    }

    private fun setUpDatesIntoRecyclerView(dao: HistoryDao, list: ArrayList<HistoryEntity>) {

        if (list.isNotEmpty()) {
            val itemAdapter = HistoryAdapter(list) { date ->
                deleteCompletedDate(dao, date)
            }
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = itemAdapter

            binding?.rvHistory?.visibility = View.VISIBLE
            binding?.tvHistory?.visibility = View.VISIBLE
            binding?.ivClearAllDates?.visibility = View.VISIBLE
            binding?.tvNoDataAvailable?.visibility = View.GONE
        } else {
            binding?.rvHistory?.visibility = View.GONE
            binding?.tvHistory?.visibility = View.GONE
            binding?.ivClearAllDates?.visibility = View.GONE
            binding?.tvNoDataAvailable?.visibility = View.VISIBLE

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}