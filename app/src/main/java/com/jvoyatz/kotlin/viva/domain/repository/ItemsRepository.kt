package com.jvoyatz.kotlin.viva.domain.repository

import androidx.lifecycle.LiveData
import com.jvoyatz.kotlin.viva.data.source.remote.dto.ItemDTO
import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.Item
import kotlinx.coroutines.flow.Flow


interface ItemsRepository {
    fun getItemsDB(): Flow<List<Item>>
    fun fetchItems(forceUpdate: Boolean = false): Flow<InitializationState>
}