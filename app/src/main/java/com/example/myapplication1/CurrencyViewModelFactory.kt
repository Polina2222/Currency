package com.example.myapplication1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyViewModelFactory(private val currencyRepository: CurrencyRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java))
            return CurrencyViewModel(currencyRepository)  as T
        throw IllegalArgumentException("ViewModel not found!")
    }
}