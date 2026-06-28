package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.FeedContent

interface FeedRepository {
    suspend fun getFeed(): FeedContent
}
