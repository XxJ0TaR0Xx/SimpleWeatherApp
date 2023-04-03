package com.l_volkov_l.simpleweatherapp.business.room

import androidx.room.*

/// объект который предоставляет нам домтуп к нашей БД
@Dao
interface GeoCodeDao {

    @Query("SELECT * FROM GeoCode")
    fun getAll(): List<GeoCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item : GeoCodeEntity)

    @Delete
    fun remove(item: GeoCodeEntity)

}