package com.samir.dinntask.ingredients.viewmodel

import OrdersViewState.ingredients.viewmodel.IngredientsActions
import com.samir.dinntask.shared.base.BaseVM
import com.samir.dinntask.shared.domain.ingredients.GetIngredientsCategoryListUseCase
import com.samir.dinntask.shared.domain.ingredients.GetIngredientsListUseCase
import com.samir.dinntask.shared.domain.ingredients.IngredientsViewState
import com.samir.dinntask.shared.domain.ingredients.IngredientsListPartialState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val getIngredientsCategoryListUseCase: GetIngredientsCategoryListUseCase,
    private val getIngredientsListUseCase: GetIngredientsListUseCase
) : BaseVM<IngredientsActions, IngredientsViewState, IngredientsListPartialState>() {

    override val initialState by lazy { IngredientsViewState() }

    override fun reduce(
        result: IngredientsListPartialState,
        previousState: IngredientsViewState
    ): IngredientsViewState {
        return result.reduce(previousState, initialState)
    }

    private val getIngredientsCategory by lazy {
        ObservableTransformer<IngredientsActions.GetIngredientsCategory,
            IngredientsListPartialState> { actions ->
            actions.flatMap {
                getIngredientsCategoryListUseCase(Unit)
            }
        }
    }

    private val getIngredients by lazy {
        ObservableTransformer<IngredientsActions.GetIngredients,
            IngredientsListPartialState> { actions ->
            actions.flatMap {
                val id = GetIngredientsListUseCase.Params.create(it.categoryId)
                getIngredientsListUseCase(id)
            }
        }
    }

    override fun handle(
        action: Observable<IngredientsActions>
    ): List<Observable<out IngredientsListPartialState>> =
        listOf(
            action.ofType(IngredientsActions.GetIngredientsCategory::class.java)
                .take(1)
                .compose(getIngredientsCategory),
            action.ofType(IngredientsActions.GetIngredients::class.java)
                .compose(getIngredients)
        )
}
