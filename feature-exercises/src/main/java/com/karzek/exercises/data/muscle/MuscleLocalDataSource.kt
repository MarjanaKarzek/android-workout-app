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
        return Single.just(isCacheValid())
            .flatMap {
                if (it) {
                    muscleDao.getAll()
                } else {
                    muscleDao.deleteAll()
                        .andThen(Single.just(emptyList()))
                }
            }.flatMapMaybe {
                if (it.isEmpty()) {
                    Maybe.empty()
                } else {
                    Maybe.just(it.toModels())
                }
            }
    }

    override fun setAllMuscles(muscles: List<Muscle>): Completable {
        return Completable.fromCallable {
            val muscleEntities = muscles.map {
                MuscleEntity(it.id, it.name)
            }
            muscleDao.insertAll(muscleEntities)
        }
    }

    private fun isCacheValid(): Boolean {
        val currentTime = Date().time
        val timestamp = sharedPreferences.getLong(MUSCLE_CACHE_TIME_STAMP, currentTime)
        return (timestamp + ONE_DAY) >= currentTime
    }

    companion object {
        private const val MUSCLE_CACHE_TIME_STAMP = "MUSCLE_CACHE_TIME_STAMP"
        private const val ONE_DAY = 24 * 60 * 60 * 1000L
    }

}
