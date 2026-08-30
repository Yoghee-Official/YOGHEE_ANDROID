package com.teamyoga.yoghee.core.domain.model

data class CreateClassPolicyParam(
    val discountPrice: Int = 0,
    val discountRate: Int = 0,
    val reservationNote: String = "",
    val refundPolicies: List<CreateRefundPolicyParam> = emptyList(),
)

data class CreateRefundPolicyParam(
    val hoursBeforeClass: Int,
    val refundRate: Int,
)
