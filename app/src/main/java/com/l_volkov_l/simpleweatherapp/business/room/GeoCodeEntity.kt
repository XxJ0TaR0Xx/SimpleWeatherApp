package com.l_volkov_l.simpleweatherapp.business.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.l_volkov_l.simpleweatherapp.business.model.LocalNames

///БД для любымых городов
@Entity(tableName = "GeoCode", primaryKeys = ["lat","lon"])
data class GeoCodeEntity (
    @ColumnInfo(name = "country") val country: String,
    @Embedded val local_names: LocalNames,// в переменной будут хранится какой-то класс
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false
)
