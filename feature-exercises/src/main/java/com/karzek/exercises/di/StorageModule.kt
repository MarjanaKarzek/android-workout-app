package com.karzek.exercises.di

import android.content.Context
import android.content.SharedPreferences
import com.karzek.exercises.database.ExerciseDatabase
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.exercise.ExerciseDao
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.relationship.ExerciseRelationsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StorageModule {

    @Singleton
    @Provides
    fun provideAppSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMuscleDao(context: Context): MuscleDao {
        return ExerciseDatabase.getDatabase(context).muscleDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(context: Context): CategoryDao {
        return ExerciseDatabase.getDatabase(context).categoryDao()
    }

    @Singleton
    @Provides
    fun provideExerciseDao(context: Context): ExerciseDao {
        return ExerciseDatabase.getDatabase(context).exerciseDao()
    }

    @Singleton
    @Provides
    fun provideEquipmentDao(context: Context): EquipmentDao {
        return ExerciseDatabase.getDatabase(context).equipmentDao()
    }

    @Singleton
    @Provides
    fun provideExerciseCategoryDao(context: Context): ExerciseRelationsDao {
        return ExerciseDatabase.getDatabase(context).exerciseRelationsDao()
    }

    private const val SHARED_PREFERENCES_NAME = "workout_app_shared_preferences"

}