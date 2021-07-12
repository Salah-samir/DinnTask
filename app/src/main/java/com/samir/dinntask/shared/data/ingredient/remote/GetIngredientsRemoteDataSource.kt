package com.samir.dinntask.shared.data.ingredient.remote

import com.samir.dinntask.model.IngredientsCategoryItem
import com.samir.dinntask.model.IngredientsItem
import com.samir.dinntask.shared.data.remote.DinDinnApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetIngredientsRemoteDataSource @Inject constructor(
    private val api: DinDinnApi
) : IGetIngredientsDataSource {

    override fun getIngredients(id: Int): Single<List<IngredientsItem>> = api.getIngredientsById(id)

    override fun getIngredientsCategory(): Single<List<IngredientsCategoryItem>> =
        api.getIngredientsCategory()

    override fun searchIngredients(query: String): Single<List<IngredientsItem>> =
        api.searchIngredients(query)
}
