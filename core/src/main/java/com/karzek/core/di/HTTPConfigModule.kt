package com.karzek.core.di

import com.karzek.core.http.Endpoints
import com.karzek.core.http.HTTPConstant.WORKOUTS_BASE_URL
import dagger.Module
import dagger.Provides

@Module
object HTTPConfigModule {

    @Provides
    fun provideEndpoints(): Endpoints {
        return Endpoints(WORKOUTS_BASE_URL)
    }
}