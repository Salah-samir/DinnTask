package com.samir.dinntask.shared.data.remote

import com.samir.dinntask.model.IngredientsCategoryItem
import com.samir.dinntask.model.IngredientsItem
import com.samir.dinntask.model.OrdersItem
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DinDinnApi {

    @GET("orders")
    fun getOrders(): Single<List<OrdersItem>>

    @GET("getIngredientsById")
    fun getIngredientsById(
        @Query("category_id") id: Int
    ): Single<List<IngredientsItem>>

    @GET("getIngredientsCategory")
    fun getIngredientsCategory(): Single<List<IngredientsCategoryItem>>

    @GET("searchIngredients")
    fun searchIngredients(
        @Query("query")
        query: String
    ): Single<List<IngredientsItem>>
}
