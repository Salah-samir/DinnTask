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

package com.samir.dinntask.ingredients.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samir.dinntask.R
import com.samir.dinntask.databinding.IngredientPageListItemView
import com.samir.dinntask.model.IngredientsItem

class IngredientsPageViewHolder(private val binding: IngredientPageListItemView) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: IngredientsItem) {
        binding.item = item
    }

    companion object {

        private fun getItemLayoutId(): Int = R.layout.ingredients_page_item

        private fun inflateItemUsing(parent: ViewGroup): IngredientPageListItemView =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getItemLayoutId(),
                parent,
                false
            )

        infix fun initializeWith(parent: ViewGroup): IngredientsPageViewHolder {
            return IngredientsPageViewHolder(inflateItemUsing(parent))
        }
    }
}
