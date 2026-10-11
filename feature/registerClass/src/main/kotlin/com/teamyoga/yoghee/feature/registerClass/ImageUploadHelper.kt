package com.teamyoga.yoghee.feature.registerClass

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.teamyoga.yoghee.core.domain.model.ImageUploadFile
import com.teamyoga.yoghee.core.domain.repository.ImageRepository
import com.teamyoga.yoghee.feature.registerClass.components.ImageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

internal const val IMAGE_UPLOAD_TYPE = "class"

internal suspend fun uploadImagesIfAny(
    imageRepository: ImageRepository,
    images: List<ImageItem>,
    context: Context,
): List<String> {
    if (images.isEmpty()) return emptyList()
    val files = withContext(Dispatchers.IO) {
        images.map { it.uri.toUploadFile(context) }
    }
    return imageRepository.uploadImages(type = IMAGE_UPLOAD_TYPE, files = files)
}

internal fun Uri.toUploadFile(context: Context): ImageUploadFile {
    val resolver = context.contentResolver
    val contentType = resolver.getType(this) ?: "image/jpeg"
    val bytes = resolver.openInputStream(this)?.use { it.readBytes() }
        ?: error("이미지를 읽을 수 없습니다: $this")
    val (width, height) = decodeImageSize(bytes)
    val fileName = queryDisplayName(context) ?: generateFileName(contentType)
    return ImageUploadFile(
        fileName = fileName,
        contentType = contentType,
        width = width,
        height = height,
        bytes = bytes,
    )
}

private fun Uri.queryDisplayName(context: Context): String? {
    val cursor = context.contentResolver.query(
        this,
        arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
        null, null, null,
    ) ?: return null
    return cursor.use {
        if (it.moveToFirst()) it.getString(0) else null
    }
}

private fun generateFileName(contentType: String): String {
    val extension = when {
        contentType.contains("png") -> "png"
        contentType.contains("gif") -> "gif"
        contentType.contains("webp") -> "webp"
        else -> "jpg"
    }
    return "${UUID.randomUUID()}.$extension"
}

// 헤더만 디코딩해서 이미지 크기 추출 (전체 비트맵 메모리 로드 X)
private fun decodeImageSize(bytes: ByteArray): Pair<Int, Int> {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    return options.outWidth.coerceAtLeast(0) to options.outHeight.coerceAtLeast(0)
}
