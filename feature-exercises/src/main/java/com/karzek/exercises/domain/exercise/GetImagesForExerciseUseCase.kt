package com.karzek.exercises.domain.exercise

import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output.Success
import com.karzek.exercises.domain.exercise.IGetImagesForExerciseUseCase.Output.SuccessNoData
import com.karzek.exercises.domain.exercise.repository.IExerciseRepository
import io.reactivex.Single
import javax.inject.Inject

class GetImagesForExerciseUseCase @Inject constructor(
    private val exerciseRepository: IExerciseRepository
) : IGetImagesForExerciseUseCase {
    override fun execute(input: Input): Single<Output> {
        return exerciseRepository.getImageUrls(input.exerciseId)
            .map {
                if (it.isEmpty()) {
                    SuccessNoData
                } else {
                    Success(it)
                }
            }
            .onErrorReturn {
                ErrorUnknown
            }
    }
}