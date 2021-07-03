package com.example.workmanagerapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert
    fun insertAll(vararg images: RecyclerItemModel): List<Long>

    @Query("SELECT * FROM RecyclerItemModel")
    suspend fun getAllImages(): List<RecyclerItemModel>


    @Query("DELETE FROM RecyclerItemModel")
    suspend fun deleteAllImages()

}