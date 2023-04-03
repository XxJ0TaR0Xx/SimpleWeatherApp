package com.l_volkov_l.simpleweatherapp.business.model

///Информация о погоде
data class WeatherDataModel(
    val current: Current,
    val daily: List<DailyWeatherModel>,
    val hourly: List<HourlyWeatherModel>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)