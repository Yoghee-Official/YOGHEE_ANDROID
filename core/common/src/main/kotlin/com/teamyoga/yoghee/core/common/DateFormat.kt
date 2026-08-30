package com.teamyoga.yoghee.core.common

// ISO-8601(예: 2026-08-01T16:18:37.131Z) 앞부분에서 yyyy-MM-dd만 추출.
// 파싱 실패 시 원본 반환.
fun formatCreatedAt(createdAt: String): String {
    val datePart = createdAt.substringBefore('T', missingDelimiterValue = "")
    return if (datePart.length == 10) datePart else createdAt
}
