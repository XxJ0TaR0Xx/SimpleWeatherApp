package com.l_volkov_l.simpleweatherapp

import android.app.Application
import android.content.Intent
import androidx.room.Room
import com.l_volkov_l.simpleweatherapp.business.room.OpenWeatherDatabase
import com.l_volkov_l.simpleweatherapp.business.room.WeatherDao
import com.l_volkov_l.simpleweatherapp.view.SettingsHolder


const val APP_SETTINGS = "App settings"
const val IS_STARTED_UP = "Is started up"// впервые ли запущено приложение ?

class App: Application() {

    companion object{// статический класс либо поле из java, компилируется раньше чем весь остальной код
        lateinit var db: OpenWeatherDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //TODO убрать fallbackToDestructiveMigration() к релизу
        db = Room.databaseBuilder(this, OpenWeatherDatabase::class.java, "OpenWeatherDB")// инициализируем всю базу целиком
            .fallbackToDestructiveMigration()//позволяет нам не настраивать эмиграцию при изменении таблицы
            .build()

        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)// сохраняет значения в виде map(сохраняет данные вне приложения)

        SettingsHolder.onCreate(preferences)

        val flag = preferences.contains(IS_STARTED_UP) // впервые ли запущено приложение ?

        if(!flag){
            // - Опевещаем о том что приложение уже запускалось
            val editor = preferences.edit()
            editor.putBoolean(IS_STARTED_UP, true)
            editor.apply()
            //
            val intent =  Intent(this,InitialActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}