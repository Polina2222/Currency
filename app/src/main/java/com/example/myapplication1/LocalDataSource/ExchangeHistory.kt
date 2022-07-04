package com.example.myapplication1.LocalDataSource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exchange")
data class ExchangeHistory(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  @SerializedName("currency1_name")
  val currency1Name: String,
  @SerializedName("currency1_count")
  val currency1Count: Float,
  @SerializedName("currency2_name")
  val currency2Name: String,
  @SerializedName("currency2_count")
  val currency2Count: Float,
  @SerializedName("date")
  val date: String
)