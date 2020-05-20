package com.karzek.core.ui

import androidx.lifecycle.ViewModel
import com.karzek.core.ui.error.UIError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    val error = BehaviorSubject.create<UIError>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}

