package com.karzek.exercises.data.muscle.contract

import com.karzek.exercises.domain.muscle.model.Muscle
import io.reactivex.Single

interface IMuscleRemoteDataSource {
    fun getAllMuscles(): Single<List<Muscle>>
}
