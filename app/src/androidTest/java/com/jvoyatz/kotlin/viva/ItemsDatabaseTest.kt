package com.jvoyatz.kotlin.viva

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jvoyatz.kotlin.viva.data.source.local.ItemsDao
import com.jvoyatz.kotlin.viva.data.source.local.ItemsDatabase
import com.jvoyatz.kotlin.viva.data.source.local.entity.ItemEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TAG = "ItemsDatabaseTest"

@RunWith(AndroidJUnit4::class)
class ItemsDatabaseTest {

    private lateinit var itemsDb: ItemsDatabase
    private lateinit var itemsDao: ItemsDao

    @Before
    fun createDatabase(){
        Log.d(TAG, "createDatabase() called")
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        itemsDb = Room.inMemoryDatabaseBuilder(context, ItemsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        itemsDao = itemsDb.itemsDao
    }

    @After
    fun closeDatabase(){
        Log.d(TAG, "closeDatabase() called")
        itemsDb.close()
    }

    @Test
    fun insertAndGetItems(){
        Log.d(TAG, "insertAndGetItems() called")
        var list = ItemEntity.createList(2)
        itemsDao.insert(list)
        var allItems = itemsDao.getItemsList()

        Assert.assertEquals(allItems?.size, 2)
    }
}