package com.karzek.exercises.data.muscle.contract

import com.karzek.exercises.domain.muscle.model.Muscle
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface IMuscleLocalDataSource {
    fun getAllMuscles(): Maybe<List<Muscle>>
    fun setAllMuscles(muscles: List<Muscle>): Completable
    fun isCacheValid(): Single<Boolean>
}
