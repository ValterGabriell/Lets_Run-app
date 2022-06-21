package com.example.runapp.db

interface RoomDataSource {
    fun addName(dataModel: DataModel)
    fun getAll(id: String): List<DataModel>
    fun getById(id: String): DataEntity
}