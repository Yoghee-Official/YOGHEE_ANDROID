package com.teamyoga.yoghee.feature.category

data class CategoryTab(
    val id: String,
    val name: String,
)

internal val CategoryTabs: List<CategoryTab> = listOf(
    CategoryTab(id = "1", name = "릴렉스"),
    CategoryTab(id = "2", name = "파워"),
    CategoryTab(id = "3", name = "초심자"),
    CategoryTab(id = "4", name = "이색요가"),
    CategoryTab(id = "5", name = "전통요가"),
)
