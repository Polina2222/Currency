package com.example.myapplication1

import android.content.Context
import com.example.myapplication1.RemoteDataSource.CurrencyApi
import com.example.myapplication1.LocalDataSource.CurrencyDao
import com.example.myapplication1.LocalDataSource.CurrencyDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RepositoryInitializer{

    private var api: CurrencyApi? = null
    private var dao: CurrencyDao? = null
    private lateinit var currencyRepository: CurrencyRepository

    fun getRepository(context: Context): CurrencyRepository {
        if (api == null) {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                    .baseUrl("http://data.fixer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(CurrencyApi::class.java)
        }
        if (dao == null)
            dao = CurrencyDatabase.getInstance(context)?.dao()

        if (api != null && dao != null)
            currencyRepository = CurrencyRepository(api!!, dao!!)
        return currencyRepository
    }
}