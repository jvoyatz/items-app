package com.jvoyatz.kotlin.viva.data.source.remote.dto

import com.squareup.moshi.Json

//network dto
data class ItemDTO(
    @Json(name = "Id") val id: Int, @Json(name = "Name") val name: String,
    @Json(name = "Price") val price: String, @Json(name = "Thumbnail") val thumbnail: String,
    @Json(name = "Image") val image: String,  @Json(name = "Description") val description: String?)