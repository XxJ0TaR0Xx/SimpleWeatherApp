package com.l_volkov_l.simpleweatherapp.presenters

import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<T :MvpView> : MvpPresenter<T>() { // BasePresenter будет зависить от интерфесов  которые реализуют интерфейс MvpView

    abstract fun enable()

}