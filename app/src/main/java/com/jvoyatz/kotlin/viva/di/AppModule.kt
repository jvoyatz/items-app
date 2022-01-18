package com.jvoyatz.kotlin.viva.di

import android.content.Context
import androidx.room.Room
import com.jvoyatz.kotlin.viva.data.ItemsRepositoryImpl
import com.jvoyatz.kotlin.viva.data.database.ItemsDao
import com.jvoyatz.kotlin.viva.data.database.ItemsDatabase
import com.jvoyatz.kotlin.viva.data.remote.ItemsApiService
import com.jvoyatz.kotlin.viva.domain.interactors.GetItemsInteractor
import com.jvoyatz.kotlin.viva.domain.interactors.FetchItemsInteractor
import com.jvoyatz.kotlin.viva.domain.interactors.Interactors
import com.jvoyatz.kotlin.viva.domain.repository.ItemsRepository
import com.jvoyatz.kotlin.viva.util.URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


/**
 * There are several ways to provide injection using hilt
 * 1) constructor injection. hilt knows this way how to provide instances of this class
 *      hilt should be aware of how to provide argument-types found in the constructor
 * !!!NOTE!!! Module classes like this found here, let Hilt know how to provide instances for particular types.
 * For instance, you might dont own some types, so you are not able to injection using the first way as described before,
 * thus you can inform HIlt how to provide these types using modules
 *
 * 2) If you have an interface, for instance a Repository interface, you are not able to inject it using the constructor.
 * In this case you can use @Binds. More to be added here!!!!!!!!!!
 * Using binds you can inform Hilt so as to be able to know implementation to use to provide
 * an instance of the given interface
 *
 * 3) Another option is the @Provides annotation
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideItemsDatabase(@ApplicationContext context: Context): ItemsDatabase {
        return Room.databaseBuilder(
                        context,
                        ItemsDatabase::class.java,
                        "items_database")
                .build()
    }

    @Provides
    @Singleton
    fun provideItemsDao(db: ItemsDatabase): ItemsDao {
        return db.itemsDao
    }

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()


    @Provides
    fun providesInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttp(interceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build();

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()


    @Provides
    fun provideItemService(retrofit: Retrofit): ItemsApiService = retrofit.create(ItemsApiService::class.java)


    @Provides
    fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO


    @Provides
    @Singleton
    fun provideItemRepository(itemsApiService: ItemsApiService, itemsDao: ItemsDao, dispatcher: CoroutineDispatcher):
            ItemsRepository = ItemsRepositoryImpl(itemsDao, itemsApiService, dispatcher)

    @Provides
    fun provideItemsInteractors(repository: ItemsRepository) =
        Interactors(
            GetItemsInteractor(repository),
            FetchItemsInteractor(repository),
        )
}