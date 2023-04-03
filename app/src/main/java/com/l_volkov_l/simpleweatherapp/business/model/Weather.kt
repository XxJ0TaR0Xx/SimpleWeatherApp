package com.l_volkov_l.simpleweatherapp.business.model

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)