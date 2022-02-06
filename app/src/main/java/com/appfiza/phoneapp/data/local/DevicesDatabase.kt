package com.appfiza.phoneapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appfiza.phoneapp.data.local.daos.DevicesDao
import com.appfiza.phoneapp.data.local.model.DeviceDAO

/**
 * Created by Fayçal KADDOURI 🐈
 */
@Database(entities = [DeviceDAO::class], version = 1)
abstract class DeviceDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "devices.db"

        private val lock = Any()

        private var INSTANCE: DeviceDatabase? = null

        fun getInstance(context: Context): DeviceDatabase {
            return INSTANCE ?: synchronized(lock) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DeviceDatabase::class.java, DATABASE_NAME
                ).build()
                    .also { INSTANCE = it }
            }
        }
    }

    abstract fun devicesDao(): DevicesDao
}