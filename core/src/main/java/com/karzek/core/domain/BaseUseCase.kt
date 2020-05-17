package com.karzek.core.domain

interface BaseUseCase<IN : BaseUseCase.Input, OUT : BaseUseCase.Output> {

    interface Input

    interface Output
}