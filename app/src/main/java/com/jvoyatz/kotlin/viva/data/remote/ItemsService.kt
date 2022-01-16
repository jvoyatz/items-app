package com.jvoyatz.kotlin.viva.data.remote

import com.jvoyatz.kotlin.viva.data.source.remote.dto.ItemDTO
import retrofit2.http.GET


interface ItemsApiService {
// old way with callbacks
//    @GET("products")
//    fun getItemsStr(): Call<String> //get as string
//    @GET("products")
//    fun getItems(): Call<List<ItemDTO>>

    @GET("products")
    suspend fun getItems(): List<ItemDTO>
}

//manual creation

//object ItemsApi {
//    private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        //.addConverterFactory(ScalarsConverterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .baseUrl(URL)
//        .build()
//
//    val service: ItemsApiService by lazy {
//        retrofit.create(ItemsApiService::class.java)
//    }
//}