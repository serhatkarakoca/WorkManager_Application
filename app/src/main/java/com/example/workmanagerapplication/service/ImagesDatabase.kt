package com.example.workmanagerapplication.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workmanagerapplication.model.RecyclerItemModel

@Database(entities = [RecyclerItemModel::class], version = 1)
abstract class ImagesDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        private var instance: ImagesDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ImagesDatabase::class.java, "imagesdatabase"
        ).build()
    }
}