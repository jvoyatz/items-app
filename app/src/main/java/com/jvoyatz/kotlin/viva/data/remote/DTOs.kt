package com.jvoyatz.kotlin.viva.data.source.remote.dto

import com.jvoyatz.kotlin.viva.data.database.entity.ItemEntity
import com.squareup.moshi.Json

/**
 * While parsing the server response, we need to transform
 * this response into a DTO object.
 */

data class ItemDTO(
    @Json(name = "Id") val id: Int, @Json(name = "Name") val name: String,
    @Json(name = "Price") val price: String, @Json(name = "Thumbnail") val thumbnail: String,
    @Json(name = "Image") val image: String,  @Json(name = "Description") val description: String?)


fun ItemDTO.toEntity(): ItemEntity =
    ItemEntity(
        id = id,
        name = name,
        price = price,
        thumbnail = thumbnail,
        image = image,
        description = description ?: ""
    )

fun List<ItemDTO>.toEntities(): List<ItemEntity> {
   return this.map {
       itemDTO -> itemDTO.toEntity()
   }
}