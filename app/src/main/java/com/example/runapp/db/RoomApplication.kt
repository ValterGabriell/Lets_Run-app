package com.example.runapp.db

import android.content.Context

class RoomApplication(context: Context) : RoomDataSource {
    private val fetchDao = DatabaseInstanceService.getInstance(context).getDao()

    override fun insertRun(dataModel: DataModel) {
        fetchDao.insertRun(DataEntity.fromModlToEntity(dataModel))
    }

    override fun getById(id: String): DataEntity {
        return fetchDao.getById(id)
    }

    override fun getAll(id:String): List<DataModel> {
        return fetchDao.getAll(id).map { it.getAll() }
    }


}