package com.example.myapplication1.ui.main

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import com.example.myapplication1.LocalDataSource.Period
import com.example.myapplication1.databinding.FragmentFilterBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.days

class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFilterBinding.inflate(layoutInflater)
        binding.period7Button.setOnClickListener {
            val period =   Period(
                startDate = LocalDate.now(),
                endDate = LocalDate.now().minusDays(7))
            savePeriod(period)
            var activity = requireActivity() as ShowFragmentCallback
            activity.showHistoryFragment(period)
        }

        binding.period30Button.setOnClickListener {
            val period =   Period(
                startDate = LocalDate.now(),
                endDate = LocalDate.now().minusDays(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH).toLong()))
            savePeriod(period)
            var activity = requireActivity() as ShowFragmentCallback
            activity.showHistoryFragment(period )
        }

        binding.periodAllButton.setOnClickListener {
            val period = Period(LocalDate.now(), LocalDate.now(), true)
            savePeriod(period)
            var activity = requireActivity() as ShowFragmentCallback
            activity.showHistoryFragment(Period(LocalDate.now(), LocalDate.now(), true))
        }

        binding.periodCustomButton.setOnClickListener {
            binding.editTextLayout.visibility = View.VISIBLE
            binding.textLayout.visibility = View.VISIBLE
            binding.applyButton.visibility = View.VISIBLE
        }
        binding.applyButton.setOnClickListener {

            var firstDate = LocalDate.parse(binding.editTextDate.text.toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            var secondDate = LocalDate.parse(binding.editTextDate2.text.toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))

            val period = Period(secondDate, firstDate)
            savePeriod(period)

            var activity = requireActivity() as ShowFragmentCallback
            activity.showHistoryFragment(period)
        }

        binding.backButton.setOnClickListener {
            val period = Period(LocalDate.now(), LocalDate.now(), true)
            savePeriod(period)
            var activity = requireActivity() as ShowFragmentCallback
            activity.showHistoryFragment(period)
        }

        return binding.root
    }

    private fun savePeriod(period: Period) {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = pref.edit()
        editor.clear()
        editor.putString("START_DATE", period.startDate.toString())
        editor.putString("END_DATE", period.endDate.toString())
        editor.putBoolean("ALL_TIME", period.allTime)
        editor.apply()
    }

}
