package com.jvoyatz.kotlin.items.ui.home

import androidx.lifecycle.*
import com.jvoyatz.kotlin.items.domain.InitializationState
import com.jvoyatz.kotlin.items.domain.Item
import com.jvoyatz.kotlin.items.domain.interactors.Interactors
import com.jvoyatz.kotlin.items.util.Resource
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

    private val _initCacheState:MutableStateFlow<InitializationState?> = MutableStateFlow(InitializationState.UNKNOWN)
    val initCacheState: StateFlow<InitializationState?> = _initCacheState

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
                .onStart { delay(300) }
                .catch {
                    Timber.d("caught an exception $it")
                    emit(InitializationState.ERROR)
                }
                .collect {
                    _initCacheState.value = it
                }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            interactors.initItems(true)
                .onStart {
                    delay(300)
                }
                .catch {
                    emit(InitializationState.ERROR)
                }
                .collect {
                    _initCacheState.value = it
                }
        }
    }
    fun onResetCacheStateValue(){
        _initCacheState.value = null
    }
    fun navigateItemDetails(item: Item) {
        _selectedItem.value = item
    }
    fun onDoneNavigating(){
        _selectedItem.value = null
    }
}