package com.teamyoga.yoghee.core.data.mapper

import com.teamyoga.yoghee.core.data.remote.model.FeedItemDto
import com.teamyoga.yoghee.core.data.remote.model.FeedResponse
import com.teamyoga.yoghee.core.domain.model.FeedContent
import com.teamyoga.yoghee.core.domain.model.FeedItem

fun FeedResponse.toDomain(): FeedContent = FeedContent(
    weekLabel = weekLabel.orEmpty(),
    items = items.orEmpty().map { it.toDomain() },
)

private fun FeedItemDto.toDomain(): FeedItem = FeedItem(
    title = title.orEmpty(),
    description = description.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
)
