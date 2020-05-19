package com.karzek.core.ui.binding

import android.view.View
import com.google.android.material.chip.ChipGroup
import com.google.android.material.chip.ChipGroup.OnCheckedChangeListener
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun ChipGroup.checkedChanges(): Observable<Int> {
    return ChipGroupCheckedChangeObservable(this)
}

class ChipGroupCheckedChangeObservable(private val view: ChipGroup) : InitialValueObservable<Int>() {

    override fun subscribeListener(observer: Observer<in Int>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        view.setOnCheckedChangeListener(listener)
        observer.onSubscribe(listener)
    }

    override val initialValue = view.checkedChipId

    internal class Listener(
        private val view: ChipGroup,
        private val observer: Observer<in Int>
    ) : MainThreadDisposable(), OnCheckedChangeListener {

        private var lastChecked: Int = View.NO_ID

        override fun onCheckedChanged(
            chipGroup: ChipGroup?,
            checkedId: Int
        ) {
            if (!isDisposed && checkedId != lastChecked) {
                lastChecked = checkedId
                observer.onNext(checkedId)
            }
        }

        override fun onDispose() {
            view.setOnCheckedChangeListener(null)
        }

    }

}