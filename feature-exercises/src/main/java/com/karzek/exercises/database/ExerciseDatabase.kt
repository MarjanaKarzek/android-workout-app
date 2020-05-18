package com.karzek.exercises.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.muscle.MuscleEntity

@Database(
    entities = [
        MuscleEntity::class,
        CategoryEntity::class
    ], version = 2, exportSchema = false
)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun muscleDao(): MuscleDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DB_NAME = "exercise_database"

        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}