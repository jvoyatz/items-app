package com.jvoyatz.kotlin.viva.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoyatz.kotlin.viva.domain.interactors.Interactors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val interactors: Interactors
    ) : ViewModel() {

    //encapsulating livedata
    private val _itemsLiveData = interactors.getItems.execute()
    val itemsLiveData
        get() = _itemsLiveData

    init {
        viewModelScope.launch {
            Timber.d("!!!!!!!!!!!!launching coroutine")
            delay(20000)
            try {
                interactors.initItems()
            }catch (e: Exception){
                Timber.e("eee ${e.message}")
            }
        }
    }

    private fun refreshItems() {
        viewModelScope.launch {
            interactors.refreshItems()
        }
    }
}

//class Factory(private val application: Application, private val repository: ItemsRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
//            return HomeViewModel(application, repository) as T
//        }
//
//        throw IllegalArgumentException("wrong class")
//    }
//}
