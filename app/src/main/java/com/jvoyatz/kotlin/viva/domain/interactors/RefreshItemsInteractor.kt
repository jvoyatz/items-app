package com.jvoyatz.kotlin.viva.domain.interactors

import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import javax.inject.Inject

class RefreshItemsInteractor @Inject constructor(
    private val repository: ItemsRepository
) {
    suspend operator fun invoke(){
        repository.refreshItems()
    }
}