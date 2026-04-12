package com.teamyoga.yoghee.core.common

/**
 * Use cases / repositories에서 공통으로 쓸 수 있게 최소 Result 타입만 둡니다.
 * (프로젝트가 커지면 error code/message 매핑이나 domain error 확장으로 발전시키기 좋습니다.)
 */
sealed interface AppError {
    val message: String?
}

data class UnknownError(
    override val message: String? = null,
) : AppError

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure(val error: AppError) : Result<Nothing>()
}

