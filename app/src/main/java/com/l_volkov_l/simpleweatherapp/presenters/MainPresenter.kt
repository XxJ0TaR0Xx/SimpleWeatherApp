package com.l_volkov_l.simpleweatherapp.presenters

import android.util.Log
import com.l_volkov_l.simpleweatherapp.business.ApiProvider
import com.l_volkov_l.simpleweatherapp.business.repos.MainRepository
import com.l_volkov_l.simpleweatherapp.view.MainView

const val MPTAG = "MainPresenterTAG"
class MainPresenter : BasePresenter<MainView>() {
    private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter
            .doAfterNext{viewState.setLocation(false)}
            .subscribe{ response ->
            //Log.d(MPTAG, "Presenter enable(): $response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let{ viewState.displayError(response.error) }
        }
    }

    fun refresh(lat : String, lon: String){
        viewState.setLocation(true)
        repo.reloadData(lat, lon)
        Log.d(MPTAG, "MainPresenter: ")
    }
}
