package com.karzek.exercises.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karzek.exercises.database.category.CategoryDao
import com.karzek.exercises.database.category.CategoryEntity
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.equipment.EquipmentEntity
import com.karzek.exercises.database.exercise.ExerciseDao
import com.karzek.exercises.database.exercise.ExerciseEntity
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.muscle.MuscleEntity
import com.karzek.exercises.database.relationship.ExerciseCategoryCrossRef
import com.karzek.exercises.database.relationship.ExerciseEquipmentCrossRef
import com.karzek.exercises.database.relationship.ExerciseMuscleCrossRef
import com.karzek.exercises.database.relationship.ExerciseRelationsDao

@Database(
    entities = [
        ExerciseEntity::class,
        MuscleEntity::class,
        CategoryEntity::class,
        EquipmentEntity::class,
        ExerciseEquipmentCrossRef::class,
        ExerciseMuscleCrossRef::class,
        ExerciseCategoryCrossRef::class
    ], version = 7, exportSchema = false
)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleDao(): MuscleDao
    abstract fun categoryDao(): CategoryDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun exerciseRelationsDao(): ExerciseRelationsDao

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