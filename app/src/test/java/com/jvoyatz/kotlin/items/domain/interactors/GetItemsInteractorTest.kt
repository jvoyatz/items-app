package com.jvoyatz.kotlin.items.domain.interactors

import com.google.common.truth.Truth.assertThat
import com.jvoyatz.kotlin.items.data.FakeItemsRepository
import com.jvoyatz.kotlin.items.domain.InitializationState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetItemsInteractorTest {

    private lateinit var getItems: GetItemsInteractor
    private lateinit var fakeRepo : FakeItemsRepository

    @Before
    fun setUp() {
        fakeRepo = FakeItemsRepository()
        getItems = GetItemsInteractor(fakeRepo)
    }

    @Test
    fun initRepository() = runBlocking {
        val state = fakeRepo.fetchItems().first()
        assertThat(state).isEqualTo(InitializationState.NOT_INITIALIZED)
    }

    @Test
    fun refreshRepository() = runBlocking {
        val state = fakeRepo.fetchItems(true).first()
        assertThat(state).isEqualTo(InitializationState.REFRESH)
    }

    @Test
    fun isInitializedRepository() = runBlocking {
        fakeRepo.mockAlreadyInit()
        val state = fakeRepo.fetchItems(false).last()
        assertThat(state).isEqualTo(InitializationState.INITIALIZED)
    }

    @Test
    fun getItems() = runBlocking {
        fakeRepo.fetchItems(true).first()
        val items = fakeRepo.getItemsDB().first()
        assertThat(items).isNotEmpty()
    }

}