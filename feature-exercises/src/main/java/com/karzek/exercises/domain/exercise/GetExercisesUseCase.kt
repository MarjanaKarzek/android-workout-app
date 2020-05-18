package com.karzek.exercises.domain.exercise

import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Input
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Output
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.exercise.IGetExercisesUseCase.Output.Success
import com.karzek.exercises.domain.exercise.repository.IExerciseRepository
import io.reactivex.Single
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val exerciseRepository: IExerciseRepository
) : IGetExercisesUseCase {
    override fun execute(input: Input): Single<Output> {
        return exerciseRepository.getExercises(input.currentPage, input.pageSize)
            .map {
                Success(it.first, it.second) as Output
            }
            .onErrorReturn {
                ErrorUnknown
            }
    }
}