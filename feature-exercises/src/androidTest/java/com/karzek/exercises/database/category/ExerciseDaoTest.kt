package com.karzek.exercises.database.category

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.karzek.exercises.database.ExerciseDatabase
import com.karzek.exercises.database.equipment.EquipmentDao
import com.karzek.exercises.database.equipment.EquipmentEntity
import com.karzek.exercises.database.exercise.ExerciseDao
import com.karzek.exercises.database.exercise.ExerciseEntity
import com.karzek.exercises.database.exercise.ExerciseEntityFull
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.muscle.MuscleEntity
import com.karzek.exercises.database.relationship.ExerciseCategoryCrossRef
import com.karzek.exercises.database.relationship.ExerciseEquipmentCrossRef
import com.karzek.exercises.database.relationship.ExerciseMuscleCrossRef
import com.karzek.exercises.database.relationship.ExerciseRelationsDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

internal class ExerciseDaoTest {
    private lateinit var dao: ExerciseDao
    private lateinit var muscleDao: MuscleDao
    private lateinit var equipmentDao: EquipmentDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var relationsDao: ExerciseRelationsDao
    private lateinit var db: ExerciseDatabase

    private val entities = listOf(
        ExerciseEntity(1, "Exercise 1", "Description 1", "imageUrl"),
        ExerciseEntity(2, "Exercise 2")
    )

    private val categories = listOf(
        CategoryEntity(1, "Category 1"),
        CategoryEntity(2, "Category 2")
    )

    private val equipments = listOf(EquipmentEntity(1, "Equipment 1"))

    private val muscles = listOf(MuscleEntity(1, "Muscle 1"))

    private val muscleRelations = listOf(ExerciseMuscleCrossRef(1, 1))

    private val categoryRelations = listOf(
        ExerciseCategoryCrossRef(1, 1),
        ExerciseCategoryCrossRef(2, 2)
    )

    private val equipmentRelations = listOf(ExerciseEquipmentCrossRef(2, 1))

    private val entitiesFull = listOf(
        ExerciseEntityFull(
            entities[0],
            listOf(categories[0]),
            listOf(muscles[0]),
            emptyList()
        ), ExerciseEntityFull(
        entities[1],
        listOf(categories[1]),
        emptyList(),
        listOf(equipments[0])
    )
    )

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, ExerciseDatabase::class.java
        ).build()
        dao = db.exerciseDao()
        categoryDao = db.categoryDao()
        equipmentDao = db.equipmentDao()
        muscleDao = db.muscleDao()
        relationsDao = db.exerciseRelationsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun successfulWriteAndReadPaged() {
        dao.insertAll(entities).test()

        categoryDao.insertAll(categories)
        muscleDao.insertAll(muscles)
        equipmentDao.insertAll(equipments)

        relationsDao.insertAllRelatedCategories(categoryRelations)
        relationsDao.insertAllRelatedMuscles(muscleRelations)
        relationsDao.insertAllRelatedEquipments(equipmentRelations)

        //todo fix
//        dao.getAllPaged().toObservable(5).test()
//            .assertValue {
//                it[0] == entitiesFull[0]
//                    && it[1] == entitiesFull[1]
//                    && it.size == 2
//            }
    }

    @Test
    @Throws(Exception::class)
    fun successfulWriteAndRead() {
        dao.insertAll(entities).test()

        categoryDao.insertAll(categories)
        muscleDao.insertAll(muscles)
        equipmentDao.insertAll(equipments)

        relationsDao.insertAllRelatedCategories(categoryRelations)
        relationsDao.insertAllRelatedMuscles(muscleRelations)
        relationsDao.insertAllRelatedEquipments(equipmentRelations)

        //todo fix
//        dao.getAll().test().assertValue(entitiesFull)
    }

    @Test
    @Throws(Exception::class)
    fun successfulCount() {
        dao.insertAll(entities).test()
        dao.countItems().test().assertValue(2)
    }

    @Test
    @Throws(Exception::class)
    fun successfulDeletion() {
        dao.insertAll(entities).test()
        dao.deleteAll().test()
        dao.getAll().test().assertValue(emptyList())
    }

    @Test
    @Throws(Exception::class)
    fun successfulOverwrite() {
        dao.insertAll(entities).test()

        categoryDao.insertAll(categories)
        muscleDao.insertAll(muscles)
        equipmentDao.insertAll(equipments)

        relationsDao.insertAllRelatedCategories(categoryRelations)
        relationsDao.insertAllRelatedMuscles(muscleRelations)
        relationsDao.insertAllRelatedEquipments(equipmentRelations)

        dao.insertAll(entities).test()

        categoryDao.insertAll(categories)
        muscleDao.insertAll(muscles)
        equipmentDao.insertAll(equipments)

        relationsDao.insertAllRelatedCategories(categoryRelations)
        relationsDao.insertAllRelatedMuscles(muscleRelations)
        relationsDao.insertAllRelatedEquipments(equipmentRelations)

        //todo fix
//        dao.getAll().test().assertValue(entitiesFull)
    }
}