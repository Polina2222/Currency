package com.example.myapplication1.RemoteDataSource

import retrofit2.http.GET

interface CurrencyApi {
    @GET("http://api.coinlayer.com/api/live?access_key=e5e3b985ae22d852f09703962dcfee91")
    suspend fun getCurrencies(): CurrencyResponse
}
