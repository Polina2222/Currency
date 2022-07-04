package com.example.myapplication1

import com.example.myapplication1.LocalDataSource.CurrencyDao
import com.example.myapplication1.LocalDataSource.CurrencyEntity
import com.example.myapplication1.LocalDataSource.ExchangeHistory
import com.example.myapplication1.RemoteDataSource.CurrencyApi
import com.example.myapplication1.RemoteDataSource.CurrencyResponse


class CurrencyRepository(private val currencyApi: CurrencyApi, private val currencyDao: CurrencyDao) {

    suspend fun getCurrencies(): CurrencyResponse {
        return currencyApi.getCurrencies()
    }

    suspend fun insertExchangeHistory(exchangeHistory: ExchangeHistory){
        currencyDao.insertExchangeHistory(exchangeHistory)
    }

    suspend fun saveCurrencies(currencies: List<CurrencyEntity>){
        currencyDao.insertCurrencies(currencies)
    }

    suspend fun updateCurrency(currencyEntity: CurrencyEntity) {
        currencyDao.updateCurrency(currencyEntity.name, currencyEntity.rate, currencyEntity.favorite)
    }

    suspend fun getCurrenciesFromLocalDataSource(): List<CurrencyEntity> {
        return currencyDao.getCurrenciesFromLocalDataSource()
    }

    suspend fun getExchangeHistory(): List<ExchangeHistory>{
        return currencyDao.getExchangeHistory()
    }
}