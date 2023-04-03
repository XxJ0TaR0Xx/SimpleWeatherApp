package com.l_volkov_l.simpleweatherapp.business.repos

import android.util.Log
import com.google.gson.Gson
import com.l_volkov_l.simpleweatherapp.business.ApiProvider
import com.l_volkov_l.simpleweatherapp.business.model.WeatherDataModel
import com.l_volkov_l.simpleweatherapp.business.room.WeatherDataEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val TAG = "MAINREPO"

class MainRepository(api: ApiProvider): BaseRepository<MainRepository.ServerResponse>(api) {
    private val gson = Gson()
    private val dbAccess = db.getWeatherDao()
    private val defLanguage = when (Locale.getDefault().displayLanguage) {
        "русский" -> "ru"
        else -> "en"
    }

    /// Заход в интернет по 2-м запросам
    fun reloadData(lat: String, lon: String){

        ///Получаем данные из интернета и форматируем их возвращая лист данных и берём первое значение
        Observable.zip(
            api.provideWeatherApi().getWeatherForecast(lat, lon, lang = defLanguage),
            api.provideGeoCodeApi().getCityByCord(lat, lon).map{
                it.asSequence()
                    .map {model ->
                        when (Locale.getDefault().displayLanguage) {
                            "русский" -> model.local_names.ru
                            "English" -> model.local_names.en
                            else -> model.name
                        }
                    }
                    .toList()
                    .filterNotNull()
                    .first()
            }
            /// Присваиваем данные
        ) { weatherData, geoCode -> ServerResponse(geoCode, weatherData) }
            .subscribeOn(Schedulers.io())
            .doOnNext {
                dbAccess.insertWeatherData(WeatherDataEntity(data = gson.toJson(it.weatherData),city = it.cityName))
            }
            ///При ошибки вызываем послудний город в нашей БД
            .onErrorResumeNext {
                Observable.just(ServerResponse(
                    dbAccess.getWeatherData().city,
                    gson.fromJson(dbAccess.getWeatherData().data, WeatherDataModel::class.java),
                    it
                ))
            }
            ///Возвращаем данные в основной поток
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    dataEmitter.onNext(it)
                },{
                    Log.d(TAG, "reloadData: $it")
                })
    }


    data class ServerResponse(val cityName: String, val weatherData: WeatherDataModel, val error: Throwable? = null)

}