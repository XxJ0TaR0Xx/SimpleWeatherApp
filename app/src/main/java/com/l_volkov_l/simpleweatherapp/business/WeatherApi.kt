package com.l_volkov_l.simpleweatherapp.business

import com.l_volkov_l.simpleweatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.8/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") appid: String = "f0a05a07b491b12e551d043238a8cf73",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>// метод будет возвращать объект обсервбл в котором будут лежать данные о погоже
}