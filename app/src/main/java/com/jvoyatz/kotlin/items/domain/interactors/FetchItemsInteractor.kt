package com.jvoyatz.kotlin.items.domain.interactors

import com.jvoyatz.kotlin.items.domain.InitializationState
import com.jvoyatz.kotlin.items.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class FetchItemsInteractor @Inject constructor(
    private val repository: ItemsRepository
) {
     operator fun invoke(forceUpdate: Boolean = false): Flow<InitializationState> {
        return flow {
            try {
                repository.fetchItems(forceUpdate)
                    .collect {
                        emit(it)
                    }
            }catch (e: Exception){
                e.printStackTrace()
                emit(InitializationState.ERROR)
            }
        }
    }
}