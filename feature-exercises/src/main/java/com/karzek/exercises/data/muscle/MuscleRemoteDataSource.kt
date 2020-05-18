package com.karzek.exercises.data.muscle

import com.karzek.exercises.data.muscle.contract.IMuscleRemoteDataSource
import com.karzek.exercises.domain.muscle.model.Muscle
import com.karzek.exercises.http.muscle.IMuscleApiService
import com.karzek.exercises.http.muscle.model.toModels
import io.reactivex.Single
import javax.inject.Inject

class MuscleRemoteDataSource @Inject constructor(
    private val muscleApiService: IMuscleApiService
) : IMuscleRemoteDataSource {

    override fun getAllMuscles(): Single<List<Muscle>> {
        return muscleApiService.getMuscles()
            .map { it.muscles.toModels() }
    }

}
