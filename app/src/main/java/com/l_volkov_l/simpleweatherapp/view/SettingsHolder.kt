package com.l_volkov_l.simpleweatherapp.view

import android.content.SharedPreferences
import android.provider.Contacts.Settings.getSetting
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDelegate
import com.l_volkov_l.simpleweatherapp.R
import java.util.*
import kotlin.math.roundToInt

// используются как ключ значение
const val TEMP = "Temp"
const val WIND_SPEED = "Wind speed"
const val PRESSURE = "Pressure"
const val THEME = "Theme"


///TODO сохраняй данные тута
object SettingsHolder {

    private lateinit var preferences: SharedPreferences
    var temp = Setting.TEMP_CELSIUS
    var windSpeed = Setting.WIND_SPEED_MS
    var pressure = Setting.PRESSURE_HPA

    fun onCreate(pref: SharedPreferences) {
        preferences = pref
        temp = getSetting(preferences.getInt(TEMP, C))// C нужно для того что бы при отсутствии ключ значения вызывать значение по умолчанию
        windSpeed = getSetting(preferences.getInt(WIND_SPEED, MS))
        pressure = getSetting(preferences.getInt(PRESSURE, HPA))
    }

    fun onDestroy() {// происходит запись всех полей
        val editor = preferences.edit()
        editor.putInt(TEMP, temp.prefConst)
        editor.putInt(WIND_SPEED, windSpeed.prefConst)
        editor.putInt(PRESSURE, pressure.prefConst)
        editor.apply()
    }

    private fun getSetting(@IdRes id: Int) = when (id) {
        C -> Setting.TEMP_CELSIUS
        F -> Setting.TEMP_FAHRENHEIT
        MS -> Setting.WIND_SPEED_MS
        KMH -> Setting.WIND_SPEED_KMH
        MM_HG -> Setting.PRESSURE_MMHG
        HPA -> Setting.PRESSURE_HPA
        else -> throw InputMismatchException()
    }


    const val C = 1
    const val F = 2
    const val MS = 3
    const val KMH = 4
    const val MM_HG = 5
    const val HPA = 6


    enum class Setting(@IdRes val checkedViewId: Int, @IdRes val mesureUnitStringRes: Int, val prefConst: Int) {

        TEMP_FAHRENHEIT(R.id.degreeF, R.string.F, F) {
            override fun getValue(initValue: Double) = valueToString { (initValue - 273.15) * (9 / 5) + 32 }
        },
        TEMP_CELSIUS(R.id.degreeC, R.string.C, C) {
            override fun getValue(initValue: Double) = valueToString { initValue - 273.15 }
        },
        WIND_SPEED_MS(R.id.speed_M, R.string.wind_speed_mu_ms, MS) {
            override fun getValue(initValue: Double) = valueToString { initValue }
        },
        WIND_SPEED_KMH(R.id.speed_H, R.string.wind_speed_mu_km, KMH) {
            override fun getValue(initValue: Double) = valueToString { initValue * 3.6 }
        },
        PRESSURE_MMHG(R.id.pressure_mm, R.string.pressure_mu_mmHg, MM_HG) {
            override fun getValue(initValue: Double) = valueToString { initValue / 1.33322387415 }
        },
        PRESSURE_HPA(R.id.pressure_h, R.string.pressure_mu_hpa, HPA) {
            override fun getValue(initValue: Double) = valueToString { initValue }
        };


        abstract fun getValue(initValue: Double): String

        protected fun valueToString(formula: () -> Double) = formula().roundToInt().toString()
    }


}

