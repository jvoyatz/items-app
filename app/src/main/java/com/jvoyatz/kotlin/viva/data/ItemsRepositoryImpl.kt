package com.jvoyatz.kotlin.viva.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jvoyatz.kotlin.viva.data.database.ItemsDao
import com.jvoyatz.kotlin.viva.data.database.entity.toDomain
import com.jvoyatz.kotlin.viva.data.remote.ItemsApiService
import com.jvoyatz.kotlin.viva.data.source.remote.dto.toEntities
import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    val items: LiveData<List<Item>> = Transformations.map(dao.getItems()){
        it.toDomain()
    }

    override fun getItemsLiveData(): LiveData<List<Item>> = items

    override suspend fun initItems() {
        Timber.d("initItems() called")
        withContext(Dispatchers.IO) {
            val dbList = dao.getItemsList()
            val isEmpty = dbList?.isEmpty() ?: true
            // if(isEmpty){
            Timber.d("no items found, fetching items")
            refreshItems()
            // }
        }
    }

    /**
     * This methods executes a database operation, so
     * it is needed to be declared as a suspend function
     */
    override suspend fun refreshItems(){
        withContext(ioDispatcher){
            Timber.i("refreshing items")
            val items = api.getItems()
            dao.insert(items = items.toEntities())
        }
    }
}
