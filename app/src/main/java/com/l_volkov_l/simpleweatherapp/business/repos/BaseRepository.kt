package com.l_volkov_l.simpleweatherapp.business.repos

import android.util.Log
import com.l_volkov_l.simpleweatherapp.App
import com.l_volkov_l.simpleweatherapp.business.ApiProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

const val BRTAG = "BaseRepositoryTAG"

abstract class BaseRepository<T>(val api: ApiProvider) {

    ///Создаём имитер и получаем ссылку на нашу БД
    val dataEmitter: BehaviorSubject<T> = BehaviorSubject.create()
    protected val db by lazy { App.db }

    ///Помещает результат вызова функции transaction в Observable и раздваивание потока с помощью 21 строки
    protected fun roomTransaction(
        transaction: () -> T
    ) = Observable.fromCallable { transaction() }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {dataEmitter.onNext(it)},{
            Log.d(BRTAG,"roomTransaction: $it ")})
}