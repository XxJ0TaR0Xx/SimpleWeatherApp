package com.l_volkov_l.simpleweatherapp.business.room

import androidx.room.*

// объект который предоставляет нам домтуп к нашей БД
@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherData WHERE id = 1")
    fun getWeatherData(): WeatherDataEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherDataEntity)

    @Update
    fun updateWeatherData(data: WeatherDataEntity)

    @Delete
    fun deleteWeatherData(data: WeatherDataEntity)



}