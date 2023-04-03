package com.l_volkov_l.simpleweatherapp.business

import com.l_volkov_l.simpleweatherapp.business.model.GeoCodeModel
import com.l_volkov_l.simpleweatherapp.business.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    this.country,
    this.local_names,
    this.lat,
    this.lon,
    this.name,
    this.state ?: "",
    this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    this.country,
    local_names,
    this.lat,
    this.lon,
    this.name,
    this.state,
    this.isFavorite
)