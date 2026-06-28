package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
    val weekLabel: String? = null,
    val items: List<FeedItemDto>? = null,
)

@Serializable
data class FeedItemDto(
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
)
