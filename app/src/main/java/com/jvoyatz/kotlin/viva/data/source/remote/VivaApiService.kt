package com.jvoyatz.kotlin.viva.data.source.remote

import com.jvoyatz.kotlin.viva.data.source.remote.dto.ItemDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val URL = "https://vivawallet.free.beeceptor.com/v1/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
   //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(URL)
    .build()

interface VivaApiService {
    //legacy - way with callbacks
//    @GET("products")
//    fun getItemsStr(): Call<String> //get as string
//    @GET("products")
//    fun getItems(): Call<List<ItemDTO>>
    @GET("products")
    suspend fun getItems(): List<ItemDTO>
}

object VivaApi {
    val vivaApiService: VivaApiService by lazy {
        retrofit.create(VivaApiService::class.java)
    }
}