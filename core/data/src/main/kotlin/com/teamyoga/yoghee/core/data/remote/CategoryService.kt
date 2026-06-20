package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.CategoryClassResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {
    @GET("api/class/category/{categoryId}")
    suspend fun getClassesByCategory(
        @Path("categoryId") categoryId: String,
        @Query("sort") sort: String,
    ): CategoryClassResponse
}
