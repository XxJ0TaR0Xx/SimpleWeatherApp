package com.l_volkov_l.simpleweatherapp.business.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/// Объявление БД с Название города и кешем для отображения данных в mainActivity
@Entity(tableName = "WeatherData")
data class WeatherDataEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo("city") val city: String,
    @ColumnInfo("json_data") val data: String
)