package com.karzek.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karzek.core.util.nonConcurrentLazy
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified T : ViewModel> bindViewModel(): Lazy<T> =
        nonConcurrentLazy {
            ViewModelProvider(this, viewModelFactory).get(T::class.java)
        }
}