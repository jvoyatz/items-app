package com.jvoyatz.kotlin.viva.domain.interactors

import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import com.jvoyatz.kotlin.viva.util.Resource
import com.jvoyatz.kotlin.viva.util.Resource.Error.Companion.create
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItemsInteractor @Inject constructor(
    private val repository: ItemsRepository
) {
     operator fun invoke(): Flow<Resource<List<Item>>>{
        return flow {
            repository.getItemsDB()
                .collect { items ->
                    try {
                        emit(Resource.Success(items))
                    }catch (e: Throwable){
                        emit(create(e))
                    }
                }
        }
    }
}