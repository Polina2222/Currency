package com.example.myapplication1.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication1.LocalDataSource.ExchangeHistory
import com.example.myapplication1.databinding.HistoryItemBinding
import com.example.myapplication1.databinding.ItemBinding

class HistoryAdapter()
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var historyList = mutableListOf<ExchangeHistory>()
    fun set(historyList: List<ExchangeHistory>) {
        this.historyList = historyList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(inflater,parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        if (position < historyList.size) {
            var  history = historyList[position]
            holder.bind(history)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class HistoryViewHolder(var binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ExchangeHistory) {
            binding.currency1Name.text = history.currency1Name
            binding.currency2Name.text = history.currency2Name
            binding.currency1amount.text = history.currency1Count.toString()
            binding.currency2amount.text = history.currency2Count.toString()
            binding.date.text = history.date


        }
    }
}
