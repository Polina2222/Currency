package com.example.myapplication1.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication1.R
import com.example.myapplication1.CurrencyData
import com.example.myapplication1.databinding.ItemBinding


class CurrencyAdapter(private var itemClickListener: ItemClickListener) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var currencies = mutableListOf<CurrencyData>()

    fun set(currencies: List<CurrencyData>){
        this.currencies = currencies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater,parent, false)
        return CurrencyViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        if (position < currencies.size) {
            var  currency = currencies[position]
            holder.bind(currency)
        }
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    class CurrencyViewHolder(var binding: ItemBinding, var itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: CurrencyData){
            binding.currencyName.text = currency.name
            binding.favorite.setImageResource(R.drawable.ic_star)

            if(currency.favorite)
                binding.favorite.setImageResource(R.drawable.ic_star2)

            binding.favorite.setOnClickListener{
                if(!currency.favorite){
                    binding.favorite.setImageResource(R.drawable.ic_star2)
                    currency.favorite = true
                }
                else {
                    binding.favorite.setImageResource(R.drawable.ic_star)
                    currency.favorite = false
                }
                itemClickListener.setCurrencyState(currency)
            }
            binding.item.setOnClickListener{
                itemClickListener.showCurrencyExchangeFragment(currency)
            }
        }

    }

}