package com.example.runapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_academia")
class DataEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "img")
    val img: ByteArray
) {

    companion object {
        fun fromModlToEntity(dataModel: DataModel) = DataEntity(
            dataModel.id,
            dataModel.img,
        )
    }

    fun getAll() = DataModel(id, img)
}



