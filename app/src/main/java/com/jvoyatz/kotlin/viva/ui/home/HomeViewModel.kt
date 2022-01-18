package com.jvoyatz.kotlin.viva.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.jvoyatz.kotlin.viva.domain.InitializationState
import com.jvoyatz.kotlin.viva.domain.Item
import com.jvoyatz.kotlin.viva.domain.interactors.Interactors
import com.jvoyatz.kotlin.viva.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val interactors: Interactors) : ViewModel() {

    private val _selectedItem: MutableLiveData<Item?> = MutableLiveData()
    val selectedItem: LiveData<Item?>
        get() = _selectedItem

    private val _initCacheState = MutableStateFlow(InitializationState.UNKNOWN)
    val initCacheState: StateFlow<InitializationState> = _initCacheState

    val itemsLiveData: LiveData<Resource<List<Item>>> = liveData {
        interactors.getItems()
            .onStart {
                emit(Resource.Loading())
                delay(600)
            }
            .catch { emit(Resource.Error.create(it)) }
            .collect {
                emit(it)
            }
    }

    init {
        viewModelScope.launch {
            interactors.initItems()
                .catch {
                    Timber.d("caught an exception $it")
                    emit(InitializationState.ERROR)
                }
                .collect {
                    _initCacheState.value = it
                }
        }
    }

    fun navigateItemDetails(item: Item) {
        _selectedItem.value = item
    }
    fun onDoneNavigating(){
        _selectedItem.value = null
    }
}

// alternative way
//    val itemsLiveData: LiveData<Resource<List<Item>>> = liveData {
//        interactors.getItems()
//            .onStart {
//                emit(Resource.Loading())
//                delay(600)
//            }
//            .catch { emit(Resource.Error.create(it)) }
//            .asLiveData()
//    }



//class Factory(private val application: Application, private val repository: ItemsRepository): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
//            return HomeViewModel(application, repository) as T
//        }
//
//        throw IllegalArgumentException("wrong class")
//    }
//}
