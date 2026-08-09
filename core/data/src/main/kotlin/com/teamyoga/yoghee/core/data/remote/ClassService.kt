package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.CenterDetailResponse
import com.teamyoga.yoghee.core.data.remote.model.CreateCenterRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateCenterResponse
import com.teamyoga.yoghee.core.data.remote.model.CreateClassRequest
import com.teamyoga.yoghee.core.data.remote.model.CreateClassResponse
import com.teamyoga.yoghee.core.data.remote.model.GetCentersResponse
import com.teamyoga.yoghee.core.data.remote.model.UpdateCenterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClassService {
    @POST("api/class")
    suspend fun createClass(
        @Body request: CreateClassRequest,
    ): CreateClassResponse

    @GET("api/center")
    suspend fun getCenters(): GetCentersResponse

    @GET("api/center/{centerId}")
    suspend fun getCenterDetail(
        @Path("centerId") centerId: String,
    ): CenterDetailResponse

    @POST("api/center")
    suspend fun createCenter(
        @Body request: CreateCenterRequest,
    ): CreateCenterResponse

    @PUT("api/center/{centerId}")
    suspend fun updateCenter(
        @Path("centerId") centerId: String,
        @Body request: CreateCenterRequest,
    ): UpdateCenterResponse
}
