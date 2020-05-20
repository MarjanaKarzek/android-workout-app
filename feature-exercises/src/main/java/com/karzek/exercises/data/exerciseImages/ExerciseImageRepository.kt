package com.karzek.exercises.data.exerciseImages

import com.karzek.exercises.data.exerciseImages.contract.IExerciseImageRemoteDataSource
import com.karzek.exercises.domain.exerciseimage.repository.IExerciseImageRepository
import io.reactivex.Single
import javax.inject.Inject

class ExerciseImageRepository @Inject constructor(
    private val exerciseImageRemoteDataSource: IExerciseImageRemoteDataSource
) : IExerciseImageRepository {

    override fun getImageUrls(exerciseId: Int): Single<List<String>> {
        return exerciseImageRemoteDataSource.getImageUrls(exerciseId)
    }

}