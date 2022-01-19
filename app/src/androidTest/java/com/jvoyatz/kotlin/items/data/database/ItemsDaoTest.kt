package com.jvoyatz.kotlin.items.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.jvoyatz.kotlin.items.data.database.entity.ItemEntity
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemsDaoTest {
    private val TAG = "ItemsDaoTest"

    /**
     * InstantTaskExecutorRule is a JUnit Rule. When you use it with the @get:Rule annotation,
     * it causes some code in the InstantTaskExecutorRule class to be run before and after the tests
     * (to see the exact code, you can use the keyboard shortcut Command+B to view the file).
     */
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ItemsDatabase
    private lateinit var dao: ItemsDao

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ItemsDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.itemsDao
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertItem() = runBlockingTest {
        val item = ItemEntity(1, "name", "price", "thumb", "image", "descr")
        dao.insert(item)

        val allItems = dao.getItemsList()
        assertThat(allItems).contains(item)
    }


    @Test
    fun insertItems() = runBlockingTest {
        val item = ItemEntity(1, "name", "price", "thumb", "image", "descr")
        dao.insert(listOf(item))

        val allItems = dao.getItemsList()
        assertThat(allItems).contains(item)
    }

    @Test
    fun getItems() = runBlockingTest {
        val item = ItemEntity(1, "name", "price", "thumb", "image", "descr")
        dao.insert(listOf(item))

        val allItems = dao.getItems().getOrAwaitValue()

        assertThat(allItems).hasSize(1)
    }

}