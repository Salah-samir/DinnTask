package OrdersViewState.ingredients.viewmodel

import com.samir.dinntask.shared.base.MVIAction

sealed class IngredientsActions : MVIAction {
    object GetIngredientsCategory : IngredientsActions()
    class GetIngredients(val categoryId: Int) : IngredientsActions()
}
