package com.l_volkov_l.simpleweatherapp.view

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.l_volkov_l.simpleweatherapp.R
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val DAY_FULL_MONTH_NAME = "dd MMMM"
const val DAY_WEEK_DAY_LONG = "dd EEEE"
const val HOUR_DOUBLE = "HH"
const val HOUR_DOUBLE_DOT_MINUTE = "HH:hh"


fun Long.toDateFormatOf(format: String): String{
    val cal = Calendar.getInstance()
    val timeZone =cal.timeZone //временная зона в которой мы находимся
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(Date(this*1000))
}

fun Double.toDegree() = SettingsHolder.temp.getValue(this)

fun Double.toPercentString(extraPart: String = "") = (this * 100).roundToInt().toString() + extraPart

fun String.provideIcon() = when(this){
    "01d" -> R.drawable.ic_01d
    "01n" -> R.drawable.ic_01n
    "02d" -> R.drawable.ic_02d //
    "02n" -> R.drawable.ic_02n //
    "03d","03n","04d","04n" -> R.drawable.ic_03dn //
    "09d","09n" -> R.drawable.ic_09dn //
    "10d" -> R.drawable.ic_10d //
    "10n" -> R.drawable.ic_10n //
    "11d","11n" -> R.drawable.ic_11dn
    "13d","13n" -> R.drawable.ic_13dn
    "50d","50n" -> R.drawable.ic_50dn
    else -> R.drawable.ic_error
}

fun String.provideImage() = when(this){
    "01d" -> R.mipmap.sun1
    "01n" -> R.mipmap.night1
    "02d" -> R.mipmap.few_clouds1
    "02n" -> R.mipmap.cloud_night
    "03d","03n","04d","04n" -> R.mipmap.cloud1
    "09d","09n","10d","10n" -> R.mipmap.rain1
    "11d","11n" -> R.mipmap.thunderstorm
    "13d","13n" -> R.mipmap.snow
    "50d","50n" -> R.mipmap.mist
    else -> R.drawable.ic_error
}

fun TextInputEditText.createObservable() : Flowable<String> { //имитим поток данных
    return Flowable.create({
        addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                it.onNext(s.toString())
            }
        })

    }, BackpressureStrategy.BUFFER)//если айтомы с помощью фкнукции onNext будут возращаться быстрее чем обробатываться, буферезует "лишную воду"
}

private abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

}