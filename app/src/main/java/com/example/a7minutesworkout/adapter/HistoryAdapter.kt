package com.example.a7minutesworkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.database.HistoryEntity
import com.example.a7minutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val dates: ArrayList<HistoryEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llHistoryItemMain = binding?.llHistoryItemMain
        val tvPosition = binding?.tvPosition
        val tvItem = binding?.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dates[position]

        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = item.date

        if(position % 2 == 0){
            holder.llHistoryItemMain.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.lightGrey)
            )
        } else {
            holder.llHistoryItemMain.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return dates.size
    }
}