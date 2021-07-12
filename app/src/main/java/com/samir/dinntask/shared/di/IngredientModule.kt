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

import com.samir.dinntask.shared.data.ingredient.remote.GetIngredientsRemoteDataSource
import com.samir.dinntask.shared.data.ingredient.remote.IGetIngredientsDataSource
import com.samir.dinntask.shared.data.ingredient.repository.GetIngredientsRepository
import com.samir.dinntask.shared.data.ingredient.repository.IGetIngredientsRepository
import com.samir.dinntask.shared.data.remote.DinDinnApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class IngredientModule {

    @Provides
    fun provideIngredientDataSource(api: DinDinnApi): IGetIngredientsDataSource =
        GetIngredientsRemoteDataSource(api)

    @Provides
    fun provideIngredientsRepository(
        getIngredientsDataSource: IGetIngredientsDataSource
    ): IGetIngredientsRepository =
        GetIngredientsRepository(getIngredientsDataSource)
}
