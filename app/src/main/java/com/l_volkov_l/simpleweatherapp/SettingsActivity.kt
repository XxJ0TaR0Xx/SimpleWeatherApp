package com.l_volkov_l.simpleweatherapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.l_volkov_l.simpleweatherapp.view.SettingsHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.view.*


const val SET = "Set"


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        inner_toolbar.setNavigationOnClickListener { onBackPressed() }

        setSavedSettings()

        listOf(groupTemp, groupSpeed, groupPressure).forEach {
            it.addOnButtonCheckedListener(
                ToggleButtonClickListener
            )
        }
        checkTheme()
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
    ///TODO - УБЕРИ ЭТО ОТСЮДА sharedPreferences

    private fun checkTheme(){
        val animationUp:Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        five.apply {
            visibility = View.INVISIBLE
        }

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Включить автомат смена
                five.apply {
                    visibility = View.INVISIBLE
                    startAnimation(animationDown)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor.putBoolean("switch_value", switch1.isChecked).apply()
                    Log.d(TAG, "Switch - ON")
                }
            } else {
                // Включить самост выб
                five.apply {
                    visibility = View.VISIBLE
                    startAnimation(animationUp)
                    editor.putBoolean("switch_value", switch1.isChecked).apply()
                }
                Log.d(TAG, "Switch - OFF")
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
            Log.d(SET,"YourselfTheme:$checkedId")
            when (checkedId){
                R.id.night -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.day -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }

    // ----------------Theme----------------

}


