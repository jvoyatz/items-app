package com.jvoyatz.kotlin.viva.domain.repository

import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.Item
import kotlinx.coroutines.flow.Flow

/**
 * Contract for the implementation found in the data layer
 */
interface ItemsRepository {
    fun getItemsDB(): Flow<List<Item>>
    fun fetchItems(forceUpdate: Boolean = false): Flow<InitializationState>
}