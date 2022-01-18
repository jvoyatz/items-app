package com.jvoyatz.kotlin.viva.domain.interactors

/**
 *  wraps tasks/use cases for executed in home screen
 */
data class Interactors(
  val getItems: GetItemsInteractor,
  val initItems: FetchItemsInteractor,
)