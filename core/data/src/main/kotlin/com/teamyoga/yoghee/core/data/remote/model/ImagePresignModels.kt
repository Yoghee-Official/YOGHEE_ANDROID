package com.teamyoga.yoghee.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ImagePresignRequest(
    val type: String,
    val files: List<ImagePresignRequestFile>,
)

@Serializable
data class ImagePresignRequestFile(
    val fileName: String,
    val contentType: String,
    val width: Int,
    val height: Int,
    val fileSize: Long,
)

@Serializable
data class ImagePresignResponse(
    val code: Int,
    val status: String,
    val data: ImagePresignData? = null,
)

@Serializable
data class ImagePresignData(
    val type: String,
    val files: List<ImagePresignResponseFile>,
)

@Serializable
data class ImagePresignResponseFile(
    val fileName: String,
    val contentType: String,
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val imageUrl: String,
    val presignedUrl: String,
)
