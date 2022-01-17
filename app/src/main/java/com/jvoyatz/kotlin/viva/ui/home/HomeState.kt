package com.jvoyatz.kotlin.viva.ui.home

import com.jvoyatz.kotlin.viva.domain.Item

data class HomeState(val items: List<Item> = listOf(), val error: String? = "")