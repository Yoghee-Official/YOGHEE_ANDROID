package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.ImageUploadFile

interface ImageRepository {

    // presign 발급 → 각 presignedUrl로 업로드 → 최종 imageUrl 리스트를 원본 순서 그대로 반환.
    // type 예: "class", "center", "profile", "license"
    suspend fun uploadImages(type: String, files: List<ImageUploadFile>): List<String>
}
