package com.jvoyatz.kotlin.viva.data

import com.jvoyatz.kotlin.viva.data.source.local.entity.ItemEntity
import com.jvoyatz.kotlin.viva.data.source.remote.dto.ItemDTO


fun ItemDTO.toEntity(): ItemEntity =
    ItemEntity(
        id = id,
        name = name,
        price = price,
        thumbnail = thumbnail,
        image = image,
        description = description ?: ""
    )