package com.example.runapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class DatabaseInstanceService : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "database"
        private var INSTANCE: DatabaseInstanceService? = null

        private fun createDatabase(context: Context): DatabaseInstanceService =
            Room.databaseBuilder(context, DatabaseInstanceService::class.java, DATABASE_NAME)
                .allowMainThreadQueries().build()

        fun getInstance(context: Context): DatabaseInstanceService =
            (INSTANCE ?: createDatabase(context).also { INSTANCE = it })

    }

    abstract fun getDao(): DataDAO


}