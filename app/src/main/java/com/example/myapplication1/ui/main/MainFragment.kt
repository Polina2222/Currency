package com.example.myapplication1.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication1.CurrencyData
import com.example.myapplication1.databinding.MainFragmentBinding
import com.example.myapplication1.CurrencyViewModel
import com.example.myapplication1.CurrencyViewModelFactory
import com.example.myapplication1.RepositoryInitializer
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.Observer

interface ItemClickListener {
    fun showCurrencyExchangeFragment(currency: CurrencyData)
    fun setCurrencyState(currency: CurrencyData)
}

class MainFragment: Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var adapter: CurrencyAdapter

    var currencyList = mutableListOf<CurrencyData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)

        val isOnline = isNetworkAvailable(requireContext())

        binding.recycleview.layoutManager = LinearLayoutManager(requireContext())

        adapter = CurrencyAdapter(object: ItemClickListener {
            override fun showCurrencyExchangeFragment(currency: CurrencyData) {
                val activityCallback = requireActivity() as ShowFragmentCallback

                var secondCurrencyName = currencyList[0].name

                if(!currencyList[0].favorite) {
                    secondCurrencyName = "USD"
                }
                if(currency.name == secondCurrencyName) {
                    Toast.makeText(requireContext(),"Выберите другую валюту!", Toast.LENGTH_SHORT).show()
                    return
                }
                activityCallback.showCurrencyExchangeFragment(currency.name, secondCurrencyName)
            }

            override fun setCurrencyState(currency: CurrencyData) {
                viewModel.updateCurrency(currency)
                for(curr in currencyList){
                    if (curr.name == currency.name)
                        curr.favorite = currency.favorite
                }
                setAdapter()
            }
        })
        binding.recycleview.adapter = adapter


        if (isOnline)
            getRemoteCurrencyList()
        else
            getLocalCurrencyList()

        return binding.root
    }

    private fun getLocalCurrencyList(){
        val liveData = viewModel.getLocalLiveDataForCurrencyList()
        viewModel.getCurrenciesFromLocalDataSource()
        liveData.observe(requireActivity(), Observer {
            liveData.value?.let {  localCurrencyList->
                for (currency in localCurrencyList)
                    currencyList.add(CurrencyData(currency.name, currency.rate, currency.favorite))
                setAdapter()
            }
        })
    }

    private fun getRemoteCurrencyList() {
        var liveData = viewModel.getData()
        viewModel.getCurrencies()
        liveData.observe(requireActivity(), Observer {
            liveData.value?.let {
                saveCurrencies(it.rates)
                showCurrencies(it.rates)
            }
        })

    }

    private fun saveCurrencies(currencies: Map<String, Float>) {
        val currenciesNames = currencies.keys.toList()
        val currenciesRates = currencies.values.toList()
        val currencyList = mutableListOf<CurrencyData>()
        for (i in currenciesNames.indices){
            currencyList.add(
                CurrencyData(
                    name = currenciesNames[i],
                    exchangeRate = currenciesRates[i],
                    favorite = false)
            )
        }

        viewModel.getCurrenciesFromLocalDataSource()
        viewModel.getLocalLiveDataForCurrencyList().observe(requireActivity(), Observer {
            it?.let {
                if (it.isEmpty()){
                    viewModel.saveCurrencies(currencyList)
                }
            }
        })
    }

    private fun showCurrencies(currencies: Map<String, Float>) {
        val currenciesNames = currencies.keys.toList()
        val currenciesRates = currencies.values.toList()
        currencyList.clear()
        for (i in currenciesNames.indices){
            currencyList.add(
                CurrencyData(
                    name = currenciesNames[i],
                    exchangeRate = currenciesRates[i],
                    favorite = false)
            )
        }

        viewModel.getCurrenciesFromLocalDataSource()
        viewModel.getLocalLiveDataForCurrencyList().observe(requireActivity(), Observer {
            it?.let { favoriteCurrencies ->
                for (favoriteCurrency in favoriteCurrencies) {
                    val currency = currencyList.find { it.name == favoriteCurrency.name }
                    if(currency != null) {
                        currencyList.remove(currency)
                        currencyList.add(
                            CurrencyData(
                                name = favoriteCurrency.name,
                                exchangeRate = favoriteCurrency.rate,
                                favorite = favoriteCurrency.favorite
                            )
                        )
                    }
                }
                setAdapter()
            }
        })
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

        val info = connectivity.allNetworkInfo

        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

    fun setAdapter() {
        currencyList.sortWith(compareBy({!it.favorite}, {it.name}))
        adapter.set(currencyList)
    }

}