package com.l_volkov_l.simpleweatherapp.view

import com.l_volkov_l.simpleweatherapp.business.model.GeoCodeModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MenuView : MvpView{

    @AddToEndSingle
    fun setLoading(flag: Boolean)

    @AddToEndSingle
    fun fillPredictionList(data: List<GeoCodeModel>)

    @AddToEndSingle
    fun fillFavoriteList(data: List<GeoCodeModel>)
}