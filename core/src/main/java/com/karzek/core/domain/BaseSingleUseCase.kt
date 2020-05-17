package com.karzek.core.domain

import io.reactivex.Single

interface BaseSingleUseCase<Input : BaseUseCase.Input, Output : BaseUseCase.Output> : BaseUseCase<Input, Output> {
    fun execute(input: Input): Single<Output>
}