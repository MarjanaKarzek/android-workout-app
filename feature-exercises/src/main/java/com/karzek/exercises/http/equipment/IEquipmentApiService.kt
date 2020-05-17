package com.karzek.exercises.http.equipment

import com.karzek.exercises.http.equipment.model.EquipmentsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface IEquipmentApiService {
    @GET("equipment/")
    fun getEquipments(): Single<EquipmentsResponse>
}