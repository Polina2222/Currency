package com.example.myapplication1.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.LocalDataSource.Period
import com.example.myapplication1.databinding.MainActivityBinding

interface ShowFragmentCallback {
    fun showHistoryFragment(period: Period)
    fun showFilterFragment()
    fun showCurrencyExchangeFragment(firstCurrency: String, secondCurrency: String)
    fun showMainFragment()
}

class MainActivity : AppCompatActivity(), ShowFragmentCallback {

    //Иногда переменную нельзя сразу инициализировать, сделать это можно чуть позже.
// Для таких случаев придумали новый модификатор lateinit (отложенная инициализация).
// Это относится только к изменяемым переменным.
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, MainFragment())
            .commit()

        binding.marketButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment())
                .commit()
        }

        binding.historyButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, HistoryFragment())
                .commit()
        }


    }

    override fun showHistoryFragment(period: Period) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, HistoryFragment())
            .commit()
    }

    override fun showFilterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, FilterFragment())
            .commit()
    }

    override fun showCurrencyExchangeFragment( firstCurrency: String, secondCurrency: String) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, CurrencyExchangeFragment(firstCurrency, secondCurrency))
            .commitNow()
    }

    override fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, MainFragment())
            .commit()
    }

}