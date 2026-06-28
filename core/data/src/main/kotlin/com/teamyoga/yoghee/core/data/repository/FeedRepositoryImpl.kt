package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.mapper.toDomain
import com.teamyoga.yoghee.core.data.remote.FeedService
import com.teamyoga.yoghee.core.domain.model.FeedContent
import com.teamyoga.yoghee.core.domain.repository.FeedRepository
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedService: FeedService,
) : FeedRepository {
    override suspend fun getFeed(): FeedContent = feedService.getFeed().toDomain()
}
