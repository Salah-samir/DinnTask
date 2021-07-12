package com.samir.dinntask.shared.data.ingredient.repository

import com.samir.dinntask.shared.data.ingredient.remote.IGetIngredientsDataSource
import com.samir.dinntask.shared.domain.ingredients.IngredientsListPartialState
import com.samir.dinntask.shared.domain.search.SearchPartialState
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetIngredientsRepository @Inject constructor(
    private val ingredientsDataSource: IGetIngredientsDataSource
) : IGetIngredientsRepository {

    override fun getIngredients(id: Int): Single<IngredientsListPartialState> {
        return ingredientsDataSource.getIngredients(id)
            .map {
                if (it.isNullOrEmpty()) {
                    IngredientsListPartialState.Empty
                } else {
                    IngredientsListPartialState.Ingredients(it)
                }
            }.onErrorReturn {
                IngredientsListPartialState.Failure(it)
            }
    }

    override fun getIngredientsCategory(): Single<IngredientsListPartialState> {
        return ingredientsDataSource.getIngredientsCategory()
            .map {
                if (it.isNullOrEmpty()) {
                    IngredientsListPartialState.Empty
                } else {
                    IngredientsListPartialState.IngredientsCategory(it)
                }
            }.onErrorReturn {
                IngredientsListPartialState.Failure(it)
            }
    }

    override fun searchIngredients(query: String): Single<SearchPartialState> {
        return ingredientsDataSource.searchIngredients(query)
            .map {
                if (it.isNullOrEmpty()) {
                    SearchPartialState.Empty
                } else {
                    SearchPartialState.Ingredients(it)
                }
            }.onErrorReturn {
                SearchPartialState.Failure(it)
            }
    }
}
