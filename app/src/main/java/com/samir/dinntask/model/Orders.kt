package com.samir.dinntask.model

data class OrdersItem(
    val quantity: Int? = null,
    val addon: List<AddonItem> = emptyList(),
    val alertedAt: String? = null,
    val createdAt: String? = null,
    val id: Int? = null,
    val expiredAt: String? = null,
    val title: String? = null
)

data class AddonItem(
    val quantity: Int? = null,
    val id: Int? = null,
    val title: String? = null
)
