package com.karzek.exercises.domain.validation

import com.karzek.exercises.data.category.CategoryRepository
import com.karzek.exercises.data.equipment.EquipmentRepository
import com.karzek.exercises.data.muscle.MuscleRepository
import com.karzek.exercises.domain.exercise.repository.IPagedExerciseProvider
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Input
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output.ErrorUnknown
import com.karzek.exercises.domain.validation.IValidateCacheUseCase.Output.Success
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ValidateCacheUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val muscleRepository: MuscleRepository,
    private val equipmentRepository: EquipmentRepository,
    private val exerciseProvider: IPagedExerciseProvider
) : IValidateCacheUseCase {

    override fun execute(input: Input): Single<Output> {
        return Completable.mergeArray(
            categoryRepository.validateCache(),
            muscleRepository.validateCache(),
            equipmentRepository.validateCache(),
            exerciseProvider.clearCache()
        ).toSingle {
            Success as Output
        }
            .onErrorReturn {
                ErrorUnknown
            }
    }

}