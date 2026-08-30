package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.di.AuthHttp
import com.teamyoga.yoghee.core.data.remote.ImageService
import com.teamyoga.yoghee.core.data.remote.model.ImagePresignRequest
import com.teamyoga.yoghee.core.data.remote.model.ImagePresignRequestFile
import com.teamyoga.yoghee.core.domain.model.ImageUploadFile
import com.teamyoga.yoghee.core.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageService: ImageService,
    // presignedUrl(외부 스토리지)로 업로드할 때는 앱의 auth 헤더가 붙으면 안 되므로 AuthHttp 클라이언트를 사용.
    @AuthHttp private val uploadHttpClient: OkHttpClient,
) : ImageRepository {

    override suspend fun uploadImages(
        type: String,
        files: List<ImageUploadFile>,
    ): List<String> {
        if (files.isEmpty()) return emptyList()

        val presignResponse = imageService.presign(
            ImagePresignRequest(
                type = type,
                files = files.map { it.toRequestFile() },
            )
        )
        val presignFiles = presignResponse.data?.files
            ?: error("presign 응답이 비어있습니다.")
        require(presignFiles.size == files.size) {
            "presign 응답 개수가 요청과 다릅니다. (요청=${files.size}, 응답=${presignFiles.size})"
        }

        // 각 파일을 병렬 업로드하되, 결과는 원본 인덱스 순서로 반환.
        return coroutineScope {
            files.mapIndexed { index, file ->
                val presign = presignFiles[index]
                async(Dispatchers.IO) {
                    uploadToPresignedUrl(presign.presignedUrl, file)
                    presign.imageUrl
                }
            }.awaitAll()
        }
    }

    private suspend fun uploadToPresignedUrl(
        presignedUrl: String,
        file: ImageUploadFile,
    ) = withContext(Dispatchers.IO) {
        val body = file.bytes.toRequestBody(file.contentType.toMediaTypeOrNull())
        val request = Request.Builder()
            .url(presignedUrl)
            .post(body)
            .build()
        uploadHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("이미지 업로드 실패: HTTP ${response.code} (${file.fileName})")
            }
        }
    }

    private fun ImageUploadFile.toRequestFile() = ImagePresignRequestFile(
        fileName = fileName,
        contentType = contentType,
        width = width,
        height = height,
        fileSize = fileSize,
    )
}
