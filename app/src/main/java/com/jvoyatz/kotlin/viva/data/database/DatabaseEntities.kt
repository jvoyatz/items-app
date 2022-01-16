package com.jvoyatz.kotlin.viva.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvoyatz.kotlin.viva.domain.Item

/**
 * Entities for reading and writing from/into the database
 * exist here
 */

/**
 * Represents an item entity in the database
 */
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

fun ItemEntity.toDomain(): Item{
    return Item(
        id = id,
        name = name,
        price = price,
        thumbnail = thumbnail,
        image = image,
        description = description
    )
}
fun List<ItemEntity>.toDomain(): List<Item>{
    return this.map {
       it.toDomain()
    }
}