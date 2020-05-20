package com.karzek.exercises.domain.muscle.repository

import io.reactivex.Completable

interface IMuscleRepository {
    fun validateCache(): Completable
}
