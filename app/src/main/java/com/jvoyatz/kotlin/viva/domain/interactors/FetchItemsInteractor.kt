package com.jvoyatz.kotlin.viva.domain.interactors

import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import com.jvoyatz.kotlin.viva.util.Resource
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
                emit(InitializationState.ERROR)
            }
        }
    }
}