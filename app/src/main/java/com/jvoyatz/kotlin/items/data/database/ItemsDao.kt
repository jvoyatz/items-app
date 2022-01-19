package com.jvoyatz.kotlin.items.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jvoyatz.kotlin.items.data.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ItemEntity>)

    //no suspend for this function, room runs it in a background thread
    //due to livedata return type
    //dispatches latest value whenever data in the database is changed
    @Query("SELECT * from items ORDER BY id ASC")
    fun getItems(): LiveData<List<ItemEntity>>

    @Query("SELECT * from items ORDER BY id ASC")
    fun getItemsFlow(): Flow<List<ItemEntity>>

    @Query("SELECT * from items ORDER BY id ASC")
    suspend fun getItemsList(): List<ItemEntity>
}