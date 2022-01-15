package com.jvoyatz.kotlin.viva.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jvoyatz.kotlin.viva.data.source.local.entity.ItemEntity

@Dao
interface ItemsDao {
    @Insert
    fun insert(item: ItemEntity)

    @Insert
    fun insert(items: List<ItemEntity>)

    @Query("SELECT * from items ORDER BY id ASC")
    fun getItems(): LiveData<List<ItemEntity>>

    @Query("SELECT * from items ORDER BY id ASC")
    fun getItemsList(): List<ItemEntity>
}