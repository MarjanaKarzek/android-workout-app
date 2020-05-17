package com.karzek.exercises.http.exercise.model

import com.google.gson.annotations.SerializedName

sealed class ExerciseThumbnailResponse {
    data class ThumbnailResponse(
        @SerializedName("original") val original: String?
    ) : ExerciseThumbnailResponse()

    object EmptyResponse : ExerciseThumbnailResponse()
}

