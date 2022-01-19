package com.jvoyatz.kotlin.items.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Models/Entities found here, are used to display data in
 * our screens
 */

/**
 * Represents an item
 */
@Parcelize
data class Item(
    val id: Int,
    val name: String,
    val price: String,
    val thumbnail: String,
    val image: String,
    val description: String?
): Parcelable

enum class InitializationState{
    UNKNOWN, NOT_INITIALIZED, INITIALIZED, ERROR, REFRESH
}
