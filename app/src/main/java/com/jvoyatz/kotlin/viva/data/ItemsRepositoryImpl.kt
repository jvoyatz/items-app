package com.jvoyatz.kotlin.viva.data

import com.jvoyatz.kotlin.viva.data.database.ItemsDao
import com.jvoyatz.kotlin.viva.data.database.entity.toDomain
import com.jvoyatz.kotlin.viva.data.remote.ItemsApiService
import com.jvoyatz.kotlin.viva.data.source.remote.dto.toEntity
import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Using repository we isolate our data sources from the rest parts
 * of this app.
 *
 * This class acts as a mediator between several data sources, in our case those data sources are:
 * # a local database
 * # a web service
 *
 * open this image to understand better the architecture of this class as well as this app's
 * @see <a href = "https://developer.android.com/codelabs/kotlin-android-training-repository/img/69021c8142d29198.png"> image</a>
 *
 *
 * In the first version we will use LiveData to dispatch our data into the UI
 */
class ItemsRepositoryImpl(
        private val dao: ItemsDao,
        private val api: ItemsApiService,
        private val ioDispatcher: CoroutineDispatcher):ItemsRepository{

    override fun getItemsDB(): Flow<List<Item>> {
        return dao.getItemsFlow()
                .map {
                    it.toDomain() //converting to domain models
                }
    }

    override fun fetchItems(forceUpdate: Boolean): Flow<InitializationState> {
        return flow {
            //while (true) {
                try {
                    val state = initItems(forceUpdate)
                    emit(state)
                } catch (e: Throwable) {
                    throw e
                }
            //}
        }
    }


    suspend fun initItems(forceUpdate: Boolean): InitializationState{
        return withContext(ioDispatcher) {
            Timber.d("${Thread.currentThread()}")
            val dbList = dao.getItemsList()
            if(forceUpdate || dbList.isEmpty()){
                Timber.d("no items found, fetching items")
                val networkItems = api.getItems()
                dao.insert(networkItems.map { it.toEntity() })
                return@withContext if (!forceUpdate) {
                        InitializationState.NOT_INITIALIZED
                    } else {
                        InitializationState.REFRESH
                    }
            }
            InitializationState.INITIALIZED
        }
    }

//    override fun fetchItems(): Flow<Boolean> {
//        return flow {
//
//            try {
//                flow {
//                    val dbItems = dao.getItemsList()
//                    val shouldFetch = dbItems?.isEmpty() ?: true
//                    Timber.d("should fetch ? $shouldFetch")
//                    if(shouldFetch) {
//                        val items = api.getItems()
//                        emit(items)
//                    }
//                    emit(listOf())
//                }
//                .catch { throw it }
//                .onEach { news ->
//                    Timber.d("isEmpty value ? ${news.isEmpty()}")
//                    if(news.isNotEmpty()) {
//                        dao.insert(news.toEntities())
//                    }
//                }
//                .flowOn(ioDispatcher)
//                .collect {
//                    emit(it.isNotEmpty())
//                }
//            }catch (e: Throwable){
//                emit(false)
//            }
//        }
//    }
}
