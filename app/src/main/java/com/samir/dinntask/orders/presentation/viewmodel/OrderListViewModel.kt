/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samir.dinntask.orders.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.samir.dinntask.shared.base.BaseVM
import com.samir.dinntask.shared.domain.orders.GetOrdersUseCase
import com.samir.dinntask.shared.domain.orders.OrdersListPartialState
import com.samir.dinntask.shared.domain.orders.OrdersViewState
import com.samir.dinntask.shared.domain.orders.RemoveOrderUseCase
import com.samir.dinntask.model.OrdersItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

const val ordersKey = "ordersKey"

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val removeOrderUseCase: RemoveOrderUseCase
) : BaseVM<OrdersListActions, OrdersViewState, OrdersListPartialState>() {

    override val initialState by lazy { OrdersViewState() }

    override fun reduce(
        result: OrdersListPartialState,
        previousState: OrdersViewState
    ): OrdersViewState {
        return result.reduce(previousState, initialState).also {
            savedStateHandle[ordersKey] = it.orders
        }
    }

    private val getOrders by lazy {
        ObservableTransformer<OrdersListActions.GetOrders, OrdersListPartialState> { actions ->
            actions.flatMap {
                val orders: List<OrdersItem>? = savedStateHandle[ordersKey]
                if (orders == null) {
                    getOrdersUseCase(Unit)
                } else {
                    Observable.just(OrdersListPartialState.Orders(orders = orders))
                }
            }
        }
    }

    private val refresh by lazy {
        ObservableTransformer<OrdersListActions.Refresh, OrdersListPartialState> { actions ->
            actions.flatMap {
                getOrdersUseCase(Unit)
            }
        }
    }

    private val removeOrder by lazy {
        ObservableTransformer<OrdersListActions.RemoveOrder, OrdersListPartialState> { actions ->
            actions.flatMap { item ->
                val params = RemoveOrderUseCase.Params.create(
                    item.order,
                    savedStateHandle[ordersKey] ?: emptyList()
                )
                removeOrderUseCase(params)
            }
        }
    }

    override fun handle(
        action: Observable<OrdersListActions>
    ): List<Observable<out OrdersListPartialState>> =
        listOf(
            action.ofType(OrdersListActions.GetOrders::class.java)
                .take(1)
                .compose(getOrders),
            action.ofType(OrdersListActions.Refresh::class.java)
                .compose(refresh),
            action.ofType(OrdersListActions.RemoveOrder::class.java)
                .compose(removeOrder)
        )
}
