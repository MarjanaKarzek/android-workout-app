package com.karzek.core.reporting

import timber.log.Timber.Tree

class CrashReportingTree : Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        //TODO implement reporting tool
    }

}
