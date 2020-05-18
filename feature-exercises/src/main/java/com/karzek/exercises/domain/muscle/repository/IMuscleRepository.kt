package com.karzek.exercises.domain.muscle.repository

import com.karzek.exercises.domain.muscle.model.Muscle
import io.reactivex.Single

interface IMuscleRepository {
    fun getAllMuscles(): Single<List<Muscle>>
}
