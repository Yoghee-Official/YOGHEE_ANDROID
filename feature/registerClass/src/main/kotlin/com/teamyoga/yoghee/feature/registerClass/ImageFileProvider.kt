package com.teamyoga.yoghee.feature.registerClass

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// 카메라 촬영 결과를 저장할 임시 URI 생성.
// 저장 위치: cacheDir/images/img_{timestamp}.jpg (file_paths.xml의 cache-path와 일치)
internal fun createImageCaptureUri(context: Context): Uri {
    val dir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
    val file = File(dir, "img_${System.currentTimeMillis()}.jpg")
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}
