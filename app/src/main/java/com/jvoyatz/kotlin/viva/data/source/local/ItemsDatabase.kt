package com.jvoyatz.kotlin.viva.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jvoyatz.kotlin.viva.data.source.local.entity.ItemEntity

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class ItemsDatabase: RoomDatabase() {
    abstract val itemsDao: ItemsDao

    companion object{
        @Volatile
        private var INSTANCE:ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase{
            synchronized(this){
                var instance = INSTANCE

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