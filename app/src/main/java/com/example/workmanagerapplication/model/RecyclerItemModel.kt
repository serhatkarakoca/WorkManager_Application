package com.example.workmanagerapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecyclerItemModel(
    @ColumnInfo(name = "url")
    var url:String?,
    @ColumnInfo(name = "status")
    var status:Boolean?,
    @ColumnInfo(name = "local_path")
    var localPath:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
