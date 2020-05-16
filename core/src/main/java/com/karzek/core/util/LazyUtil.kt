package com.karzek.core.util

fun <T> nonConcurrentLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)