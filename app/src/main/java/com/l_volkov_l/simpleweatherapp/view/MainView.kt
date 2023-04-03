package com.l_volkov_l.simpleweatherapp.view

import com.l_volkov_l.simpleweatherapp.business.model.DailyWeatherModel
import com.l_volkov_l.simpleweatherapp.business.model.HourlyWeatherModel
import com.l_volkov_l.simpleweatherapp.business.model.WeatherDataModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView{



    @AddToEndSingle // действие не будет записанно в нашу очередь раз за разом(сотрёт существующую и запишет туда новую)
    fun displayLocation (data: String) // если действие требует сохранять прогесс  то нужно использовать @AddToEnd

    @AddToEndSingle
    fun displayCurrentData (data: WeatherDataModel)

    @AddToEndSingle
    fun displayHourlyData (data: List<HourlyWeatherModel>)

    @AddToEndSingle
    fun displayDailyData (data: List<DailyWeatherModel>)

    @AddToEndSingle
    fun displayError (error: Throwable)

    @AddToEndSingle
    fun setLocation (flag: Boolean )

}