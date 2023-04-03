package com.l_volkov_l.simpleweatherapp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

abstract class DailyBaseFragment<T> : Fragment() {

    protected var mData : T? = null
    protected lateinit var fn : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fn = (context as FragmentActivity).supportFragmentManager // с помощью этого приклепляемся к майн активити
    }

    fun setData(data: T){// данные передаются в мДата и после если фрагмент отабражён вызывается функция
        mData = data
        if(isVisible){// isVisible если отображен труе иначе фолсе
            updateView()
        }
    }

    protected abstract fun updateView()
}