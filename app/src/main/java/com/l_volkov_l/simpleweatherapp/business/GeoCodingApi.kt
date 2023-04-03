package com.l_volkov_l.simpleweatherapp.business

import com.l_volkov_l.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/direct")
    fun getCityByName(// по названию города получаем координаты
        @Query("q") name: String,
        @Query("limit") limit: String = "10",
        @Query("appid") appid: String = "f0a05a07b491b12e551d043238a8cf73"
    ): Observable<List<GeoCodeModel>>

    @GET("geo/1.0/reverse?")
    fun getCityByCord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") id: String = "f0a05a07b491b12e551d043238a8cf73"
    ) : Observable<List<GeoCodeModel>>

}
