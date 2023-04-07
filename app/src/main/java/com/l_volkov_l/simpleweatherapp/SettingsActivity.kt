package com.l_volkov_l.simpleweatherapp


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButtonToggleGroup
import com.l_volkov_l.simpleweatherapp.view.SettingsHolder
import kotlinx.android.synthetic.main.activity_settings.*


const val SET = "Set"
const val SWITCH_STATE = "switchState"
const val FIVE_STATE = "fiveState"
const val DAY= "day"
const val NIGHT = "night"


class SettingsActivity : AppCompatActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        switch1.isChecked = sharedPreferences.getBoolean(SWITCH_STATE, false)
        five.visibility = sharedPreferences.getInt(FIVE_STATE, 0)
        day.isChecked = sharedPreferences.getBoolean(DAY, false)
        night.isChecked = sharedPreferences.getBoolean(NIGHT, false)


        inner_toolbar.setNavigationOnClickListener { onBackPressed() }

        setSavedSettings()


        listOf(groupTemp, groupSpeed, groupPressure).forEach {
            it.addOnButtonCheckedListener(
                ToggleButtonClickListener
            )
        }
        checkTheme()

    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.edit().putBoolean(SWITCH_STATE, switch1.isChecked).apply()
        sharedPreferences.edit().putInt(FIVE_STATE, five.visibility).apply()
        sharedPreferences.edit().putBoolean(DAY, day.isChecked).apply()
        sharedPreferences.edit().putBoolean(NIGHT, night.isChecked).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
    }

    private fun setSavedSettings() { // инициализируем наши нажатые кнопки
        groupSpeed.check(SettingsHolder.windSpeed.checkedViewId)
        groupTemp.check(SettingsHolder.temp.checkedViewId)
        groupPressure.check(SettingsHolder.pressure.checkedViewId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private object ToggleButtonClickListener : MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId to isChecked) {
                R.id.degreeC to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_CELSIUS
                R.id.degreeF to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_FAHRENHEIT
                R.id.speed_M to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_MS
                R.id.speed_H to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_KMH
                R.id.pressure_mm to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_MMHG
                R.id.pressure_h to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_HPA
            }

        }
    }


    // ----------------Theme----------------
    private fun checkTheme() {
        val animationUp:Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)


        ///Выбор темы приложения (авто или нет)
        switch1.setOnCheckedChangeListener { _, _ ->
            if (switch1.isChecked) {
                five.apply {
                    startAnimation(animationDown)
                    visibility = View.INVISIBLE
                    Log.d(SET, "Switch - ON")
                }
            } else {
                five.apply {
                    startAnimation(animationUp)
                    visibility = View.VISIBLE
                    Log.d(SET, "Switch - OFF")
                }
            }
        }

        groupDayNight.addOnButtonCheckedListener(
            YourselfTheme
        )




    }


    private object YourselfTheme : MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId){
                R.id.night -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.day -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    // ----------------Theme----------------

}


