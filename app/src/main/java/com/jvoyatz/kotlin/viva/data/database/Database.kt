package com.jvoyatz.kotlin.viva.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.jvoyatz.kotlin.viva.data.database.entity.ItemEntity

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ItemEntity>)

    //no suspend for this function, room runs it in a background thread
    //due to livedata return type
    //dispatches latest value whenever data in the database is changed
    @Query("SELECT * from items ORDER BY id ASC")
    fun getItems(): LiveData<List<ItemEntity>>

    @Query("SELECT * from items ORDER BY id ASC")
    suspend fun getItemsList(): List<ItemEntity>
}



@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class ItemsDatabase: RoomDatabase() {
    abstract val itemsDao: ItemsDao

    companion object{
        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        //we want to prevent multiple instances of the database
        fun getInstance(context: Context): ItemsDatabase {
            synchronized(this){
                var instance = INSTANCE

//                ::INSTANCE.isInitialized
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemsDatabase::class.java,
                        "items_database"
                        )
                        .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}