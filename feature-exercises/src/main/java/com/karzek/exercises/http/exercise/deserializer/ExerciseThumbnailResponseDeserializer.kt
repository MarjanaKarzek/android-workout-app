package com.karzek.exercises.http.exercise.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.EmptyResponse
import com.karzek.exercises.http.exercise.model.ExerciseThumbnailResponse.ThumbnailResponse
import java.lang.reflect.Type

class ExerciseThumbnailResponseDeserializer : JsonDeserializer<ExerciseThumbnailResponse> {

    private val gson = Gson()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ExerciseThumbnailResponse {
        var response: ExerciseThumbnailResponse? = getThumbnailResponse(json)
        if (response != null) {
            return response
        }

        response = tryEmptyResponse(json)
        if (response != null) {
            return response
        }

        throw JsonParseException("Response type not supported")
    }

    private fun getThumbnailResponse(json: JsonElement?): ThumbnailResponse? {
        return try {
            gson.fromJson(json, ThumbnailResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun tryEmptyResponse(json: JsonElement?): EmptyResponse? {
        return try {
            val list = json?.asJsonArray
            if (list != null && list.size() == 0) {
                EmptyResponse
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}