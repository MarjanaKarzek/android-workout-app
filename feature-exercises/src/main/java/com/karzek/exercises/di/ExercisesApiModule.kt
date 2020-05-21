package com.karzek.exercises.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karzek.core.di.NetworkingModule
import com.karzek.core.http.Endpoints
import com.karzek.exercises.http.category.ICategoryApiService
import com.karzek.exercises.http.equipment.IEquipmentApiService
import com.karzek.exercises.http.exercise.IExerciseApiService
import com.karzek.exercises.http.exercise.IExerciseImagesApiService
import com.karzek.exercises.http.exercise.deserializer.ExerciseThumbnailResponseDeserializer
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import com.karzek.exercises.http.muscle.IMuscleApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkingModule::class])
class ExercisesApiModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().registerTypeAdapter(
            ExerciseThumbnailResponse::class.java,
            ExerciseThumbnailResponseDeserializer()
        ).create()
    }

    @Singleton
    @Provides
    fun providesExerciseRetrofit(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        gson: Gson,
        endpoints: Endpoints
    ): Retrofit {
        okHttpClientBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Accept", "application/json").build()
            chain.proceed(request)
        }

        if(endpoints.customSocketFactory != null && endpoints.customTrustManager != null) {
            okHttpClientBuilder.sslSocketFactory(
                endpoints.customSocketFactory!!,
                endpoints.customTrustManager!!
            )
        }

        return retrofitBuilder
            .baseUrl(endpoints.workoutsBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClientBuilder.build())
            .build()
    }

    @Singleton
    @Provides
    fun providesExerciseApiService(
        retrofit: Retrofit
    ): IExerciseApiService {
        return retrofit.create(IExerciseApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesExerciseImagesApiService(
        retrofit: Retrofit
    ): IExerciseImagesApiService {
        return retrofit.create(IExerciseImagesApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesCategoryApiService(
        retrofit: Retrofit
    ): ICategoryApiService {
        return retrofit.create(ICategoryApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesEquipmentApiService(
        retrofit: Retrofit
    ): IEquipmentApiService {
        return retrofit.create(IEquipmentApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesMuscleApiService(
        retrofit: Retrofit
    ): IMuscleApiService {
        return retrofit.create(IMuscleApiService::class.java)
    }

}