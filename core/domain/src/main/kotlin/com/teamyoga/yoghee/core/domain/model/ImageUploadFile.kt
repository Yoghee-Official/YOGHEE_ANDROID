package com.teamyoga.yoghee.core.domain.model

// 업로드할 이미지 파일 하나. bytes에는 실제 파일 바이트가 담긴다.
data class ImageUploadFile(
    val fileName: String,
    val contentType: String,
    val width: Int,
    val height: Int,
    val bytes: ByteArray,
) {
    val fileSize: Long get() = bytes.size.toLong()
}
