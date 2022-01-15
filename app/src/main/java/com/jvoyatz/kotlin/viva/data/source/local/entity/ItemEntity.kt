package com.jvoyatz.kotlin.viva.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Collections.list

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: String,
    val thumbnail: String,
    val image: String,
    val description: String?
){
    companion object Factory{
        fun create(id: Int) = ItemEntity(
            id,
            "name : $id",
            price = "price $id euros",
            "thumb..$id",
            image = "image...$id",
            null)

        fun createList(size: Int): List<ItemEntity> {
            val list = ArrayList<ItemEntity>(size)

            for (i in 1..size){
               list.add(create(i))
            }

            return list
        }
    }
}