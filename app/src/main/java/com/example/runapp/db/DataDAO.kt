package com.example.runapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDAO {

    @Insert
    fun insertRun(dataEntity: DataEntity)

    @Query("SELECT * FROM tabela_academia WHERE id =:id")
    fun getAll(id: String): List<DataEntity>

    @Query("SELECT * FROM tabela_academia WHERE id =:id")
    fun getById(id: String): DataEntity

}