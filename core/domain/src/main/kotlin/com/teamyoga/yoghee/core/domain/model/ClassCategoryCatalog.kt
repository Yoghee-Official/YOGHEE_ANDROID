package com.teamyoga.yoghee.core.domain.model

// 서버와 약속한 ClassCategory code → 노출명 매핑.
// 수련 등록(선택지), 상세(아이콘/라벨) 등 여러 곳에서 공통으로 참조한다.
object ClassCategoryCatalog {

    val TYPES: List<Pair<String, String>> = listOf(
        "ashtanga" to "아쉬탕가",
        "iyengar" to "아헹가",
        "hatha" to "하타",
        "vinyasa" to "빈야사",
        "yin_yoga" to "인요가",
        "therapy" to "테라피",
        "meditation" to "명상",
        "sivananda" to "쉬바난다",
        "bepros_flow" to "비프로스플로우",
        "inside_flow" to "인사이드플로우",
        "flying_yoga" to "플라잉 요가",
        "props_yoga" to "소도구 요가",
        "others" to "기타",
    )

    val CATEGORIES: List<Pair<String, String>> = listOf(
        "unique_yoga" to "이색 요가",
        "traditional_yoga" to "전통 요가",
        "power" to "파워",
        "relax" to "릴렉스",
        "flow" to "플로우",
        "outdoor" to "야외",
        "indoor" to "실내",
        "advanced" to "숙련자",
        "beginner" to "초심자",
    )

    val USERS: List<Pair<String, String>> = listOf(
        "partner_yoga" to "파트너 요가",
        "prenatal_yoga" to "임산부 요가",
        "kids_yoga" to "키즈 요가",
        "women_only" to "여성 전용",
        "men_only" to "남성 전용",
        "unisex" to "남녀 공용",
        "pet_yoga" to "펫요가",
    )

    // 코드 → 노출명 빠른 조회용 통합 맵.
    val NAME_BY_CODE: Map<String, String> = (TYPES + CATEGORIES + USERS).toMap()
}
