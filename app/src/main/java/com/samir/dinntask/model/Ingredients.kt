package com.samir.dinntask.model

data class IngredientsItem(
    val image: String? = null,
    val id: Int? = null,
    val title: String = "",
    val imageType: String? = null,
    val type: String? = null,
    val available: Int? = 0
)

data class IngredientsCategoryItem(
    val id: Int? = null,
    val title: String? = null
)
