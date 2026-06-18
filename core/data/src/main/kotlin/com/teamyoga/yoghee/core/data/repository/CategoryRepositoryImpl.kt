package com.teamyoga.yoghee.core.data.repository

import com.teamyoga.yoghee.core.data.mapper.toDomain
import com.teamyoga.yoghee.core.data.remote.CategoryService
import com.teamyoga.yoghee.core.domain.model.CategoryClass
import com.teamyoga.yoghee.core.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryService: CategoryService,
) : CategoryRepository {
    override suspend fun getClassesByCategory(categoryId: String): List<CategoryClass> =
        categoryService.getClassesByCategory(categoryId).data.map { it.toDomain() }
}
