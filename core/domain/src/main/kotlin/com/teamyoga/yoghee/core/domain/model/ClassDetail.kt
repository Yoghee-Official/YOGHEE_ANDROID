package com.teamyoga.yoghee.core.domain.model

// GET /api/class/{classId} 응답의 Domain 모델. 서버가 필드를 누락해도 UI가 별도 null 체크를 하지 않도록 non-null 로 정의한다.
data class ClassDetail(
    val classId: String,
    val type: String,
    val name: String,
    val description: String,
    val price: Int,
    val originalPrice: Int,
    val discountRate: Int,
    val images: List<String>,
    val thumbnail: String,
    val categories: List<ClassCategory>,
    val features: List<ClassFeature>,
    val favoriteCount: Int,
    val isFavorite: Boolean,
    val reviewCount: Int,
    val rating: Double,
    val recentReviews: List<ClassReview>,
    val policy: ClassPolicy?,
    val schedules: List<ClassSchedule>,
    val scheduleYear: Int,
    val scheduleMonth: Int,
    val scheduleMonths: List<ClassScheduleMonth>,
    val tickets: List<ClassTicket>,
    val center: ClassCenter?,
    val masterInfo: ClassMasterInfo?,
)

data class ClassCategory(
    val code: String,
    val name: String,
)

data class ClassFeature(
    val code: String,
    val description: String,
)

data class ClassReview(
    val reviewId: String,
    val userUuid: String,
    val nickname: String,
    val userLevel: Int,
    val profileUrl: String,
    val thumbnail: String,
    val content: String,
    val rating: Double,
    val createdAt: String,
)

data class ClassReviewPage(
    val rating: Double,
    val reviews: List<ClassReview>,
    val page: Int,
    val totalPages: Int,
    val totalCount: Int,
    val hasNext: Boolean,
)

data class ClassPolicy(
    val discountRate: Int,
    val discountStartDate: String,
    val discountEndDate: String,
    val reservationNote: String,
    val refundPolicies: List<ClassRefundPolicy>,
)

data class ClassRefundPolicy(
    val hoursBeforeClass: Int,
    val refundRate: Int,
)

data class ClassSchedule(
    val scheduleId: String,
    val sessionId: String,
    val sessionStatus: String,
    val currentCount: Int,
    val remainingCapacity: Int,
    val bookable: Boolean,
    val unavailableReason: String,
    val alreadyReserved: Boolean,
    val dayOfWeek: Int,
    val specificDate: String,
    val startTime: String,
    val endTime: String,
    val minCapacity: Int,
    val maxCapacity: Int,
    val content: String,
    val instructorName: String,
    val effectiveFrom: String,
    val effectiveTo: String,
)

data class ClassScheduleMonth(
    val year: Int,
    val month: Int,
)

data class ClassTicket(
    val ticketId: String,
    val ticketType: String,
    val price: Int,
    val originalPrice: Int,
    val discountRate: Int,
    val validMonths: Int,
    val weeklyCount: Int,
    val totalSessions: Int,
)

data class ClassCenter(
    val centerId: String,
    val name: String,
    val description: String,
    val thumbnail: String,
    val sido: String,
    val sigungu: String,
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
    val amenities: List<String>,
    val facilities: List<String>,
)

data class ClassMasterInfo(
    val nickname: String,
    val profileImage: String,
    val introduction: String,
    val career: Int,
    val certificate: String,
)
