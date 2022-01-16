package com.jvoyatz.kotlin.viva.domain.interactors

import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import javax.inject.Inject

class InitItemsInteractor @Inject constructor(
    private val repository: ItemsRepository
) {
    suspend operator fun invoke(){
        repository.initItems()
    }
}