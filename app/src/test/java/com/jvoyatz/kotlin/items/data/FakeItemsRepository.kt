package com.jvoyatz.kotlin.items.data

import com.jvoyatz.kotlin.items.data.database.entity.ItemEntity
import com.jvoyatz.kotlin.items.data.database.entity.toDomain
import com.jvoyatz.kotlin.items.data.source.remote.dto.toEntity
import com.jvoyatz.kotlin.items.domain.InitializationState
import com.jvoyatz.kotlin.items.domain.Item
import com.jvoyatz.kotlin.items.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class FakeItemsRepository: ItemsRepository {
    private val items = mutableListOf<ItemEntity>()


    fun mockAlreadyInit(){
        val item = ItemEntity(1, "name", "price", "thumb", "image", "descr")
        items.add(item)
    }

    override fun getItemsDB(): Flow<List<Item>> {
        println(items)
        return flow {
            emit(items.map { it.toDomain() })
        }

    }

    override fun fetchItems(forceUpdate: Boolean): Flow<InitializationState> {
        return flow {
            val dbList = items
            if(forceUpdate || dbList.isEmpty()){
                //from network..
                val item = ItemEntity(2, "name2", "price2", "thumb", "image", "descr")
                items.add(item)
                if(!forceUpdate) {
                    emit(InitializationState.NOT_INITIALIZED)
                }else{
                    emit(InitializationState.REFRESH)
                }
            }
            emit(InitializationState.INITIALIZED)
        }
    }
}