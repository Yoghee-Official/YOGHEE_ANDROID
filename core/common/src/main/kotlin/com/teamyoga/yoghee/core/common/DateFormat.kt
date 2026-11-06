package com.teamyoga.yoghee.core.common

// ISO-8601(예: 2026-08-01T16:18:37.131Z) 앞부분에서 yyyy-MM-dd만 추출.
// 파싱 실패 시 원본 반환.
fun formatCreatedAt(createdAt: String): String {
    val datePart = createdAt.substringBefore('T', missingDelimiterValue = "")
    return if (datePart.length == 10) datePart else createdAt
}

/** ISO-8601 날짜를 리뷰 화면용 한국어 날짜로 변환한다. */
fun formatReviewCreatedAt(createdAt: String): String {
    val parts = createdAt.substringBefore('T').split('-')
    if (parts.size != 3) return createdAt

    val (year, month, day) = parts.mapNotNull { it.toIntOrNull() }
        .takeIf { it.size == 3 }
        ?: return createdAt
    return "%d년 %02d월 %02d일".format(year, month, day)
}
