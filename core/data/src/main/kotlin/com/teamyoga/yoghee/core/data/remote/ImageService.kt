package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.ImagePresignRequest
import com.teamyoga.yoghee.core.data.remote.model.ImagePresignResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageService {

    @POST("api/image/presign")
    suspend fun presign(@Body request: ImagePresignRequest): ImagePresignResponse
}
