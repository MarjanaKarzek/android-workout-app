package com.karzek.exercises.data.muscle

import android.content.SharedPreferences
import com.karzek.exercises.data.muscle.contract.IMuscleLocalDataSource
import com.karzek.exercises.database.muscle.MuscleDao
import com.karzek.exercises.database.muscle.MuscleEntity
import com.karzek.exercises.database.muscle.toModels
import com.karzek.exercises.domain.muscle.model.Muscle
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class MuscleLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val muscleDao: MuscleDao
) : IMuscleLocalDataSource {

    override fun getAllMuscles(): Maybe<List<Muscle>> {
        return isCacheValid()
            .flatMapMaybe {
                if (it) {
                    muscleDao.getAll()
                        .map { entities ->
                            entities.toModels()
                        }
                        .toMaybe()
                } else {
                    Maybe.empty()
                }
            }
    }

    override fun setAllMuscles(muscles: List<Muscle>): Completable {
        return Completable.fromCallable {
            muscleDao.deleteAll()
            val muscleEntities = muscles.map {
                MuscleEntity(it.id, it.name)
            }
            muscleDao.insertAll(muscleEntities)
            sharedPreferences.edit().putLong(MUSCLE_CACHE_TIME_STAMP, Date().time).commit()
        }
    }

    override fun isCacheValid(): Single<Boolean> {
        return muscleDao.countItems()
            .map {
                if (it > 0) {
                    val currentTime = Date().time
                    val timestamp = sharedPreferences.getLong(MUSCLE_CACHE_TIME_STAMP, 0)
                    (timestamp + ONE_DAY) > currentTime
                } else {
                    false
                }
            }
    }

    companion object {
        private const val MUSCLE_CACHE_TIME_STAMP = "MUSCLE_CACHE_TIME_STAMP"
        private const val ONE_DAY = 24 * 60 * 60 * 1000L
    }

}
