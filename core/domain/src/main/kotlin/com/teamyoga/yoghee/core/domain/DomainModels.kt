package com.teamyoga.yoghee.core.domain

/**
 * 메인 화면의 각 아이템을 대표하는 Sealed Interface
 * 아이템의 타입이 달라도 하나의 리스트로 관리할 수 있게 해줍니다.
 */
sealed interface MainItem {
    val id: String

    // 배너 아이템
    data class Banner(
        override val id: String,
        val imageUrl: String,
        val linkUrl: String
    ) : MainItem

    // 상품 아이템
    data class Product(
        override val id: String,
        val name: String,
        val price: Int,
        val imageUrl: String,
        val discountRate: Int = 0
    ) : MainItem

    // 광고 아이템
    data class Ad(
        override val id: String,
        val adTitle: String,
        val adContent: String,
        val actionUrl: String
    ) : MainItem
}

data class Member(
    val memberId: String,
    val name: String,
)

data class Category(
    val categoryId: String,
    val title: String,
)

/**
 * 메인 화면 데이터를 가져오는 Repository 인터페이스
 * 실제 구현은 :core:data 모듈에서 진행합니다.
 */
interface MainRepository {
    suspend fun getMainItems(): List<MainItem>
}

interface MemberRepository {
    suspend fun getMe(): Member
}

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
