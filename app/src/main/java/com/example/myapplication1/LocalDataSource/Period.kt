package com.example.myapplication1.LocalDataSource

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Period(
    var startDate: LocalDate,
    var endDate: LocalDate,
    var allTime: Boolean = false)