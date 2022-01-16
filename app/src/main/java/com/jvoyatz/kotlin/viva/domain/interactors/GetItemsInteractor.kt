package com.jvoyatz.kotlin.viva.domain.interactors

import androidx.lifecycle.LiveData
import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import javax.inject.Inject

class GetItemsInteractor @Inject constructor(
    private val repository: ItemsRepository
) {

    fun execute(): LiveData<List<Item>> {
        return repository.getItemsLiveData()
    }
}