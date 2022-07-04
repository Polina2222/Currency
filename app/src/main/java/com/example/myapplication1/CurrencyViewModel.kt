package com.example.myapplication1

import androidx.lifecycle.*
import com.example.myapplication1.LocalDataSource.CurrencyEntity
import com.example.myapplication1.LocalDataSource.ExchangeHistory
import com.example.myapplication1.RemoteDataSource.CurrencyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyViewModel(private val currencyRepository: CurrencyRepository): ViewModel() {

    private val liveData = MutableLiveData<CurrencyResponse>()
    private val localLiveDataForCurrencyList = MutableLiveData<List<CurrencyEntity>>()
    private val exchangeHistoryLiveData = MutableLiveData<List<ExchangeHistory>>()

    fun getCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        liveData.postValue(currencyRepository.getCurrencies())
    }

    fun getData(): LiveData<CurrencyResponse>{
        return liveData
    }

    fun getLocalLiveDataForCurrencyList(): LiveData<List<CurrencyEntity>>{
        return localLiveDataForCurrencyList
    }

    fun getExchangeHistoryLiveData(): LiveData<List<ExchangeHistory>>{
        return exchangeHistoryLiveData
    }

    fun saveCurrencies(currencies: List<CurrencyData>) = viewModelScope.launch(Dispatchers.IO) {
        val entities = mutableListOf<CurrencyEntity>()
        for (currency in currencies)
            entities.add(CurrencyEntity(currency.name, currency.exchangeRate, currency.favorite))
        currencyRepository.saveCurrencies(entities)
    }

    fun updateCurrency(currency: CurrencyData) = viewModelScope.launch(Dispatchers.IO) {
        currencyRepository.updateCurrency(CurrencyEntity(currency.name, currency.exchangeRate, currency.favorite))
    }

    fun getCurrenciesFromLocalDataSource() = viewModelScope.launch(Dispatchers.IO) {
        localLiveDataForCurrencyList.postValue(currencyRepository.getCurrenciesFromLocalDataSource())
    }

    fun insertExchangeHistory(exchangeHistory: ExchangeHistory) = viewModelScope.launch(Dispatchers.IO) {
        currencyRepository.insertExchangeHistory(exchangeHistory)
    }

    fun getExchangeHistory() = viewModelScope.launch(Dispatchers.IO) {
        exchangeHistoryLiveData.postValue(currencyRepository.getExchangeHistory())
    }
}