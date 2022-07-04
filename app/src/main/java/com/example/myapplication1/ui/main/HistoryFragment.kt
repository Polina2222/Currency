package com.example.myapplication1.ui.main

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication1.LocalDataSource.ExchangeHistory
import com.example.myapplication1.databinding.FragmentHistoryBinding
import com.example.myapplication1.CurrencyViewModel
import com.example.myapplication1.CurrencyViewModelFactory
import com.example.myapplication1.LocalDataSource.Period
import com.example.myapplication1.RepositoryInitializer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HistoryFragment() : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var period: Period

    private var historyList = mutableListOf<ExchangeHistory>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory)[CurrencyViewModel::class.java]

        loadPreferences()

        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        var adapter = HistoryAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.getExchangeHistoryLiveData().observe(requireActivity(), Observer {
            it?.let {
                val historyList = filterHistory(it)
                this.historyList = it.toMutableList()
                adapter.set(historyList)
            }
        })
        viewModel.getExchangeHistory()

        binding.filterButton.setOnClickListener {
            var activity = requireActivity() as ShowFragmentCallback
            activity.showFilterFragment()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadPreferences() {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pref.apply {
            if (getString("START_DATE", "") != "" &&
                getString("END_DATE", "") != ""
            ) {

                val startDate = LocalDate.parse(
                    getString("START_DATE", ""),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
                val endDate = LocalDate.parse(
                    getString("END_DATE", ""),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
                val allTime = getBoolean("ALL_TIME", false)

                period = Period(startDate, endDate, allTime)
            } else
                period = Period(LocalDate.now(), LocalDate.now(), true)
        }
    }

    private fun filterHistory(historyList: List<ExchangeHistory>): List<ExchangeHistory> {
        if (period.allTime) return historyList

        var result = mutableListOf<ExchangeHistory>()
        for (historyItem in historyList) {
            var date = LocalDate.parse(historyItem.date)
            if (date <= period.startDate && date >= period.endDate)
                result.add(historyItem)
        }
        return result
    }
}
