package com.karzek.exercises.data.muscle

import com.karzek.exercises.data.muscle.contract.IMuscleLocalDataSource
import com.karzek.exercises.data.muscle.contract.IMuscleRemoteDataSource
import com.karzek.exercises.domain.muscle.model.Muscle
import com.karzek.exercises.domain.muscle.repository.IMuscleRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MuscleRepository @Inject constructor(
    private val muscleRemoteDataSource: IMuscleRemoteDataSource,
    private val muscleLocalDataSource: IMuscleLocalDataSource
) : IMuscleRepository {

    override fun validateCache(): Completable {
        return muscleLocalDataSource.isCacheValid()
            .flatMapCompletable { isValid ->
                if (isValid) {
                    Completable.complete()
                } else {
                    updateCache()
                }
            }
    }

    private fun updateCache(): Completable {
        return muscleRemoteDataSource.getAllMuscles()
            .flatMapCompletable {
                muscleLocalDataSource.setAllMuscles(it)
            }
    }

}