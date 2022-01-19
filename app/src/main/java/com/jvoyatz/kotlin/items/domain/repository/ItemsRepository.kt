package com.jvoyatz.kotlin.items.domain.repository

import com.jvoyatz.kotlin.items.domain.InitializationState
import com.jvoyatz.kotlin.items.domain.Item
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the implementation found in the data layer
 */
interface ItemsRepository {
    fun getItemsDB(): Flow<List<Item>>
    fun fetchItems(forceUpdate: Boolean = false): Flow<InitializationState>
}