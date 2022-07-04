package com.example.myapplication1.LocalDataSource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("favorite")
    val favorite: Boolean
)