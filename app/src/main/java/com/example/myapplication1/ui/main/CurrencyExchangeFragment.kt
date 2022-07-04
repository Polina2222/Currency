package com.example.myapplication1.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.CurrencyViewModel
import com.example.myapplication1.CurrencyViewModelFactory
import com.example.myapplication1.LocalDataSource.ExchangeHistory
import com.example.myapplication1.RepositoryInitializer
import com.example.myapplication1.databinding.FragmentCurrencyExchangeBinding
import java.time.LocalDate
import java.util.*
import kotlin.math.floor

class CurrencyExchangeFragment(var firstCurrencyName: String, var secondCurrencyName: String) : Fragment(){

    private lateinit var binding: FragmentCurrencyExchangeBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    var firstCurrencyRate = 0f
    var secondCurrencyRate = 0f


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModelFactory = CurrencyViewModelFactory(RepositoryInitializer.getRepository(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)
        binding = FragmentCurrencyExchangeBinding.inflate(layoutInflater)

        binding.firstCurrency.text = firstCurrencyName
        binding.secondCurrency.text = secondCurrencyName

        viewModel.getCurrencies()
        viewModel.getData().observe(requireActivity(), Observer {
            it?.let{ currencies ->
                var currencyNames = currencies.rates.keys.toList()
                var currencyRates = currencies.rates.values.toList()
                for (i in 0..currencyNames.size-1){
                    if(firstCurrencyName == currencyNames[i])
                        firstCurrencyRate = currencyRates[i]
                    if(secondCurrencyName == currencyNames[i])
                        secondCurrencyRate = currencyRates[i]
                }
            }
        })
        binding.firstValue.addTextChangedListener {
            dynamicTextChange(binding.firstValue, binding.secondValue, false)
        }

        binding.secondValue.addTextChangedListener {
            dynamicTextChange(binding.secondValue, binding.firstValue, true)
        }

        binding.exchangeButton.setOnClickListener {
            if (binding.firstValue.text.toString() != "" && binding.secondValue.text.toString() != "") {
                var firstCurrencyAmount = binding.firstValue.text.toString().toFloat()
                var secondCurrencyAmount = binding.secondValue.text.toString().toFloat()
                viewModel.insertExchangeHistory(
                    ExchangeHistory(
                        currency1Name = firstCurrencyName,
                        currency1Count = firstCurrencyAmount,
                        currency2Name = secondCurrencyName,
                        currency2Count = secondCurrencyAmount,
                        date = LocalDate.of(
                            LocalDate.now().year,
                            LocalDate.now().month,
                        LocalDate.now().dayOfMonth).toString())
                )

                var activityCallback = requireActivity() as ShowFragmentCallback
                activityCallback.showMainFragment()
            }
            else
                Toast.makeText(requireContext(), "Введите значение!", Toast.LENGTH_SHORT).show()

        }

        binding.backButton.setOnClickListener {
            var activityCallback = requireActivity() as ShowFragmentCallback
            activityCallback.showMainFragment()
        }

        return binding.root
    }

    private fun dynamicTextChange(editTextChanged: EditText, editText: EditText, isBackExchange: Boolean){
        if (editTextChanged.isFocused) {
            try {
                if (!isBackExchange)
                    editText.setText(calculate(editTextChanged.text.toString().toFloat(),
                        firstCurrencyRate!!, secondCurrencyRate!!).toString())
                else
                    editText.setText(calculate(editTextChanged.text.toString().toFloat(),
                        secondCurrencyRate!!, firstCurrencyRate!!).toString())

            } catch (e: Exception) {
                editText.setText("0.0")
            }
        }
    }

    private fun calculate(count: Float, firstCurrencyRate: Float, secondCurrencyRate: Float): Float {
        return floor(count / firstCurrencyRate * secondCurrencyRate * 100.0f) / 100.0f
    }
}
