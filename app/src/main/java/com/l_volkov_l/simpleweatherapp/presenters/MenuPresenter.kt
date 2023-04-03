package com.l_volkov_l.simpleweatherapp.presenters

import android.util.Log
import com.l_volkov_l.simpleweatherapp.view.MenuView
import com.l_volkov_l.simpleweatherapp.business.ApiProvider
import com.l_volkov_l.simpleweatherapp.business.model.GeoCodeModel
import com.l_volkov_l.simpleweatherapp.business.repos.MenuRepository
import com.l_volkov_l.simpleweatherapp.business.repos.FAVORITE

class MenuPresenter : BasePresenter<MenuView>() {
    private val repo = MenuRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe{// подписываемся на наш датаимитер
            viewState.setLoading(false)// данные пришли и мы горовим о конце загрузки
            if (it.purpose == FAVORITE){
                Log.d("123321","enable: SAVED ${it.data}")
                viewState.fillFavoriteList(it.data)
            }else{
                Log.d("123321", "enable: CURRENT: ${it.data}")
                viewState.fillPredictionList(it.data)
            }
        }
    }

    fun searchFor(str: String){
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel){
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel){
        repo.add(data)
    }

    fun getFavoriteList(){
        repo.updateFavorite()
    }
}