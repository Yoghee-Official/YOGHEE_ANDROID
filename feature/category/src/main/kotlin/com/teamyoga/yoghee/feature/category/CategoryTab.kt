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

internal val LocationTabs: List<CategoryTab> = listOf(
    CategoryTab(id = "서울", name = "서울"),
    CategoryTab(id = "경기도", name = "경기도"),
    CategoryTab(id = "경상도", name = "경상도"),
    CategoryTab(id = "강원도", name = "강원도"),
    CategoryTab(id = "전라도", name = "전라도"),
    CategoryTab(id = "충청도", name = "충청도"),
    CategoryTab(id = "제주도", name = "제주도"),
    CategoryTab(id = "기타", name = "기타"),
)
