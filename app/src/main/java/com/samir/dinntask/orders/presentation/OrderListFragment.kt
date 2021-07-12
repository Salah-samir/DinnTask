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

package com.samir.dinntask.orders.presentation

import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.samir.dinntask.R
import com.samir.dinntask.databinding.OrdersListFragmentView
import com.samir.dinntask.orders.presentation.adapter.IActionHandler
import com.samir.dinntask.orders.presentation.adapter.OnOrdersListener
import com.samir.dinntask.orders.presentation.adapter.OrdersAdapter
import com.samir.dinntask.orders.presentation.viewmodel.OrderListViewModel
import com.samir.dinntask.orders.presentation.viewmodel.OrdersListActions
import com.samir.dinntask.utils.rxRefreshes
import com.samir.dinntask.model.OrdersItem
import com.samir.dinntask.shared.domain.orders.OrdersListPartialState
import com.samir.dinntask.shared.domain.orders.OrdersViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import android.content.res.AssetFileDescriptor


@AndroidEntryPoint
class OrderListFragment : Fragment(), IActionHandler, OnOrdersListener {

    lateinit var binding: OrdersListFragmentView
    private val ordersViewModel: OrderListViewModel by viewModels()
    private val actionSubject = PublishSubject.create<OrdersItem>()
    private lateinit var player: MediaPlayer
    private val smsSound by lazy {
        requireActivity().assets.openFd("sms.mp3")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(OrdersListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@OrderListFragment
            adapter = OrdersAdapter(this@OrderListFragment)
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ordersViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {
            }

        ordersViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) { Timber.e(it) }

        intents().apply(ordersViewModel::processIntents)
    }

    private fun render(vs: OrdersViewState) {
        binding.viewState = vs
    }

    private fun render(partialState: OrdersListPartialState) {
    }

    override fun retry() {
    }

    override fun onIngredients() {
        findNavController().navigate(R.id.action_orderListFragment_to_ingredientsFragment)
    }

    private fun intents() = Observable.merge(
        Observable.just(OrdersListActions.GetOrders),
        binding.swipeRefreshLayout.rxRefreshes().map { OrdersListActions.Refresh },
        actionSubject.map {
            OrdersListActions.RemoveOrder(it)
        }
    )

    override fun onButtonClick(order: OrdersItem?) {
        stopAlertMusic()
        order?.let {
            actionSubject.onNext(order)
        }
    }

    private fun stopAlertMusic() {
        if (::player.isInitialized && player.isPlaying) {
            player.stop()
        }
    }

    override fun playAlertMusic() {
        try {
            player = MediaPlayer()
            player.setDataSource(
                smsSound.fileDescriptor,
                smsSound.startOffset,
                smsSound.length
            )
            smsSound.close()
            player.prepare()
            player.setVolume(1f, 1f)
            player.isLooping = false
            player.start()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
