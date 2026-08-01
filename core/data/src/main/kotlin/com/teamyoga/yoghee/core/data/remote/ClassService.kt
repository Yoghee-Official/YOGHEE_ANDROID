package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateClassResponse
import com.teamyoga.yoghee.core.data.remote.model.GetCentersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ClassService {
    @POST("api/class")
    suspend fun createClass(
        @Body request: CreateClassRequest,
    ): CreateClassResponse

    @GET("api/center")
    suspend fun getCenters(): GetCentersResponse
}
