package com.karzek.core.util

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.doOnIoObserveOnMain(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
}