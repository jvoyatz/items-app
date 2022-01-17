package com.jvoyatz.kotlin.viva.domain


/**
 * Models/Entities found here, are used to display data in
 * our screens
 */

/**
 * Represents an item
 */
data class Item(
    val id: Int,
    val name: String,
    val price: String,
    val thumbnail: String,
    val image: String,
    val description: String?
)

enum class InitializationState{
    UNKNOWN, NOT_INITIALIZED, INITIALIZED, ERROR, REFRESH
}
