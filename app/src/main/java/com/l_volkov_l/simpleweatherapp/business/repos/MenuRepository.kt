package com.l_volkov_l.simpleweatherapp.business.repos

import android.util.Log
import com.l_volkov_l.simpleweatherapp.business.ApiProvider
import com.l_volkov_l.simpleweatherapp.business.mapToEntity
import com.l_volkov_l.simpleweatherapp.business.mapToModel
import com.l_volkov_l.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

const val TAK = "MENUREPO"
const val FAVORITE = 1///Подсказки
const val CURRENT = 0
class MenuRepository(api: ApiProvider) : BaseRepository<MenuRepository.Content>(api) {

    private val dbAccess = db.getGeoCodeDao()

    ///Получаем данные из интернета, отправляем в бэк и после принимаем из бэка в глав поток
    fun getCities(name: String) {
        api.provideGeoCodeApi().getCityByName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Content(it, CURRENT)
            }
            .subscribe ({
                dataEmitter.onNext(it)
            },{
                Log.d(TAK, "relod DATA:$it")
            })
    }

    ///Добавление городов
    fun add(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.add(data.mapToEntity()) }
        Log.d(TAK, "add: ${data}")
    }

    ///Удаление городов
    fun remove(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.remove(data.mapToEntity()) }
        Log.d(TAK, "remove: ${data}")
    }

    ///Обновление городов
    fun updateFavorite() {
        getFavoriteListWith()
        Log.d(TAK, "updateFavorite:")
    }

    ///Получаем список любимых городов
    private fun getFavoriteListWith(daoQuery: (() -> Unit)? = null) {
        roomTransaction {
            daoQuery?.let { it() }
            Content(dbAccess.getAll().map { it.mapToModel() }, FAVORITE)
        }
    }


    data class Content(val data: List<GeoCodeModel>, val purpose: Int)

}