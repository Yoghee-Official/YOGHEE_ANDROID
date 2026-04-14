package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.MainResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {
    @GET("api/main")
    suspend fun getMainData(
        @Query("type") type: String
    ): MainResponse
}
