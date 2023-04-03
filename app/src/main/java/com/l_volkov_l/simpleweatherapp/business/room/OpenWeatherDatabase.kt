package com.l_volkov_l.simpleweatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

///
@Database(entities = [WeatherDataEntity::class,GeoCodeEntity::class], exportSchema = false, version = 1)// version при эмиграции базы
abstract class OpenWeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    abstract fun getGeoCodeDao(): GeoCodeDao
}