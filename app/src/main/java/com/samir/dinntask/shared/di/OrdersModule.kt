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

package com.samir.dinntask.shared.di

import com.samir.dinntask.shared.data.orders.remote.GetOrdersRemoteDataSource
import com.samir.dinntask.shared.data.orders.remote.IGetOrdersDataSource
import com.samir.dinntask.shared.data.orders.repository.GetOrdersRepository
import com.samir.dinntask.shared.data.orders.repository.IGetOrdersRepository
import com.samir.dinntask.shared.data.remote.DinDinnApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class OrdersModule {

    @Provides
    fun provideOrderDataSource(api: DinDinnApi): IGetOrdersDataSource =
        GetOrdersRemoteDataSource(api)

    @Provides
    fun provideOrderRepository(
        getOrdersDataSource: IGetOrdersDataSource
    ): IGetOrdersRepository =
        GetOrdersRepository(getOrdersDataSource)
}
