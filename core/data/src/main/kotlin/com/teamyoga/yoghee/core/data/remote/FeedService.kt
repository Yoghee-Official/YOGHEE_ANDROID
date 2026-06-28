package com.teamyoga.yoghee.core.data.remote

import com.teamyoga.yoghee.core.data.remote.model.FeedResponse
import retrofit2.http.GET

interface FeedService {
    @GET("api/feed")
    suspend fun getFeed(): FeedResponse
}
