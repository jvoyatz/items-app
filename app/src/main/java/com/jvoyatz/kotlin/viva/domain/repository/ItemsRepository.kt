package com.jvoyatz.kotlin.viva.domain.repository

import androidx.lifecycle.LiveData
import com.jvoyatz.kotlin.viva.domain.Item

interface ItemsRepository {
    fun getItemsLiveData(): LiveData<List<Item>>
    suspend fun initItems()
    suspend fun refreshItems()
}