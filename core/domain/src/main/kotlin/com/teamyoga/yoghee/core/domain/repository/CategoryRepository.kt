package com.teamyoga.yoghee.core.domain.repository

import com.teamyoga.yoghee.core.domain.model.CategoryClass

interface CategoryRepository {
    suspend fun getClassesByCategory(categoryId: String): List<CategoryClass>
}
