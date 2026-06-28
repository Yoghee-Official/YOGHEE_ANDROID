package com.teamyoga.yoghee.core.domain.model

data class FeedContent(
    val weekLabel: String,
    val items: List<FeedItem>,
)

data class FeedItem(
    val title: String,
    val description: String,
    val imageUrl: String,
)
