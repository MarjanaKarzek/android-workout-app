package com.karzek.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkingModule {

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
    }


    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

}
