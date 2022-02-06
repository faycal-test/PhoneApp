package com.appfiza.phoneapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Fayçal KADDOURI 🐈
 */
@Entity(tableName = "devices")
data class DeviceDAO(
    @PrimaryKey val id: Int,
    val manufacturer: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean,
    val model: String,
    val image: String,
    val price: String,
    val description: String,
    @Embedded val specs: SpecsDAO,
    val stock: Int
)
