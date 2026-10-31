package com.teamyoga.yoghee.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamyoga.yoghee.core.domain.model.ClassReview
import com.teamyoga.yoghee.core.domain.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ReviewListUiState {
    data object Loading : ReviewListUiState
    data class Success(
        val reviews: List<ClassReview>,
        val page: Int,
        val totalPages: Int,
        val totalCount: Int,
        val hasNext: Boolean,
        val sort: String,
        val isLoadingNextPage: Boolean = false,
        val nextPageError: String? = null,
    ) : ReviewListUiState
    data class Error(val message: String) : ReviewListUiState
}

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val classRepository: ClassRepository,
) : ViewModel() {
    private val query = MutableStateFlow<ReviewQuery?>(null)
    private val _uiState = MutableStateFlow<ReviewListUiState>(ReviewListUiState.Loading)
    val uiState: StateFlow<ReviewListUiState> = _uiState.asStateFlow()
    private var firstPageJob: Job? = null

    init {
        observeQuery()
    }

    fun load(classId: String) {
        if (classId.isBlank()) {
            _uiState.value = ReviewListUiState.Error("리뷰를 불러오지 못했습니다.")
            return
        }
        if (query.value?.classId == classId) return
        query.value = ReviewQuery(classId, DEFAULT_REVIEW_SORT)
    }

    fun changeSort(sort: String) {
        if (sort !in REVIEW_SORT_OPTIONS) return
        query.update { current ->
            current?.takeIf { it.sort != sort }?.copy(sort = sort) ?: current
        }
    }

    private fun observeQuery() {
        viewModelScope.launch {
            query
                .filterNotNull()
                .distinctUntilChanged()
                .collect { request ->
                    firstPageJob?.cancel()
                    firstPageJob = viewModelScope.launch {
                        _uiState.value = ReviewListUiState.Loading
                        try {
                            val page = classRepository.getClassReviews(
                                classId = request.classId,
                                page = 0,
                                sort = request.sort,
                            )
                            if (query.value == request) {
                                _uiState.value = ReviewListUiState.Success(
                                    reviews = page.reviews,
                                    page = page.page,
                                    totalPages = page.totalPages,
                                    totalCount = page.totalCount,
                                    hasNext = page.hasNext,
                                    sort = request.sort,
                                )
                            }
                        } catch (cancellation: CancellationException) {
                            throw cancellation
                        } catch (error: Exception) {
                            if (query.value == request) {
                                _uiState.value = ReviewListUiState.Error(
                                    error.message ?: "리뷰를 불러오지 못했습니다.",
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadNextPage() {
        val current = _uiState.value as? ReviewListUiState.Success ?: return
        val currentQuery = query.value ?: return
        if (current.sort != currentQuery.sort || !current.hasNext || current.isLoadingNextPage) return

        _uiState.value = current.copy(isLoadingNextPage = true, nextPageError = null)
        viewModelScope.launch {
            runCatching {
                classRepository.getClassReviews(
                    classId = currentQuery.classId,
                    page = current.page + 1,
                    sort = currentQuery.sort,
                )
            }.onSuccess { nextPage ->
                if (query.value != currentQuery) return@onSuccess
                _uiState.update { state ->
                    val latest = state as? ReviewListUiState.Success ?: return@update state
                    latest.copy(
                        reviews = (latest.reviews + nextPage.reviews).distinctBy(ClassReview::reviewId),
                        page = nextPage.page,
                        totalPages = nextPage.totalPages,
                        totalCount = nextPage.totalCount,
                        hasNext = nextPage.hasNext,
                        isLoadingNextPage = false,
                    )
                }
            }.onFailure { error ->
                if (query.value != currentQuery) return@onFailure
                _uiState.update { state ->
                    val latest = state as? ReviewListUiState.Success ?: return@update state
                    latest.copy(
                        isLoadingNextPage = false,
                        nextPageError = error.message ?: "리뷰를 더 불러오지 못했습니다.",
                    )
                }
            }
        }
    }

    private data class ReviewQuery(
        val classId: String,
        val sort: String,
    )

    companion object {
        const val DEFAULT_REVIEW_SORT = "recent"
        val REVIEW_SORT_OPTIONS = listOf("recent", "oldest", "desc", "asc")
    }
}
