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

package com.samir.dinntask.orders.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.samir.dinntask.R
import com.samir.dinntask.databinding.OrderItemLayout
import com.samir.dinntask.utils.backendGeneralFormat
import com.samir.dinntask.utils.parseToDate
import com.samir.dinntask.model.OrdersItem
import java.util.Date

class OrdersViewHolder(private val binding: OrderItemLayout) :
    RecyclerView.ViewHolder(binding.root) {

    private var countDown: CountDownTask? = null
    private var actionHandler: OnOrdersListener? = null
    var cancelled: Boolean = false

    fun bind(item: OrdersItem) {
        binding.item = item
        binding.isExpired = isExpiredItem(item.expiredAt)
        binding.addonAdapter = AddonsAdapter(item.addon)
        configureCountDownTask(item)
        binding.acceptButton.setOnClickListener {
            if (!cancelled) {
                countDown?.cancel()
            }
            actionHandler?.onButtonClick(item)
        }
    }

    private fun isExpiredItem(expiredAt: String?): Boolean {
        val expiredAtDate = expiredAt?.parseToDate(backendGeneralFormat)?.time ?: 0
        return Date().time > expiredAtDate
    }

    private fun configureCountDownTask(item: OrdersItem) {
        val currentTime = Date().time
        val expiredAtDate = item.expiredAt?.parseToDate(backendGeneralFormat)?.time ?: 0
        val alertAtDate = item.alertedAt?.parseToDate(backendGeneralFormat)?.time ?: 0

        val expiredDifferenceInMills = expiredAtDate - currentTime
        val alertDifferenceInMills = alertAtDate - currentTime

        countDown = CountDownTask(
            expiredDifferenceInMills,
            alertDifferenceInMills,
            onAlertTriggered = {
                actionHandler?.playAlertMusic()
            },
            onFinished = {
                cancelled = true
                binding.isExpired = true
            }
        ) {
            binding.orderRemainingTime.text = it
        }
        countDown?.start()
    }

    companion object {

        private fun getItemLayoutId(): Int = R.layout.orders_list_item

        private fun inflateItemUsing(parent: ViewGroup): OrderItemLayout =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getItemLayoutId(),
                parent,
                false
            )

        infix fun initializeWith(parent: ViewGroup): OrdersViewHolder {
            return OrdersViewHolder(inflateItemUsing(parent))
        }
    }

    infix fun andSetButtonClickHandler(actionHandler: OnOrdersListener): OrdersViewHolder {
        this.actionHandler = actionHandler
        return this
    }
}
