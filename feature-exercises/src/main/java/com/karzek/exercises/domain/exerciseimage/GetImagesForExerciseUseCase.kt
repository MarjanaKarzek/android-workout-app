package com.karzek.exercises.domain.exerciseimage

import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Input
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.Success
import com.karzek.exercises.domain.exerciseimage.IGetImagesForExerciseUseCase.Output.SuccessNoData
import com.karzek.exercises.domain.exerciseimage.repository.IExerciseImageRepository
import io.reactivex.Single
import javax.inject.Inject

class GetImagesForExerciseUseCase @Inject constructor(
    private val exerciseImageRepository: IExerciseImageRepository
) : IGetImagesForExerciseUseCase {

    override fun execute(input: Input): Single<Output> {
        return exerciseImageRepository.getImageUrls(input.exerciseId)
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