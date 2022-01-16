package com.jvoyatz.kotlin.viva.domain.interactors

data class Interactors(
  val getItems: GetItemsInteractor,
  val initItems: InitItemsInteractor,
  val refreshItems: RefreshItemsInteractor
)