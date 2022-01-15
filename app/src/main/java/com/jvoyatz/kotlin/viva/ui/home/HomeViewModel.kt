package com.jvoyatz.kotlin.viva.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jvoyatz.kotlin.viva.data.source.remote.VivaApi
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(/*arg: Int*/): ViewModel() {
    //encapsulating livedata
    private val _itemsLiveData = MutableLiveData<String>()
    val itemsLiveData
        get() = _itemsLiveData

    init {

        _itemsLiveData.postValue("test init")
    }

    fun getItems(){
        _itemsLiveData.value = "test items"
//        VivaApi.vivaApiService.getItemsStr().enqueue(object: Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _itemsLiveData.value = response.body()
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _itemsLiveData.value = "fail: ${t.message}"
//            }
//        })
//        VivaApi.vivaApiService.getItems().enqueue(object: Callback<List<ItemDTO>>{
//            override fun onResponse(call: Call<List<ItemDTO>>, response: Response<List<ItemDTO>>) {
//                var str = response.body()?.joinToString { it.toString() }
//                _itemsLiveData.value = "size ${response.body()?.size} fetched $str"
//            }
//
//            override fun onFailure(call: Call<List<ItemDTO>>, t: Throwable) {
//                _itemsLiveData.value = "fail: ${t.message}"
//            }
//        })

        viewModelScope.launch {
            try {
                val items = VivaApi.vivaApiService.getItems()
                var str = items.joinToString { it.toString() }
                _itemsLiveData.value = "size ${items.size} fetched $str"
            }catch (e: Exception){
                _itemsLiveData.value = "fail: ${e.message}"
            }
        }
    }


//    class Factory(val arg: Int): ViewModelProvider.Factory{
//         override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return modelClass.getConstructor(Int::class.java)
//                .newInstance(arg)
//        }
//
////        override fun <T : ViewModel> create(modelClass: Class<T>): T {
////            TODO("Not yet implemented")
////        }
//    }
}