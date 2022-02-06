package com.appfiza.phoneapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appfiza.phoneapp.data.local.model.DeviceDAO
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fayçal KADDOURI 🐈
 */
@Dao
interface DevicesDao {

    @Query("SELECT * FROM devices")
    suspend fun getAll(): List<DeviceDAO>

    @Query("SELECT * FROM devices WHERE is_favorite = 1")
    suspend fun getAllFavourites(): List<DeviceDAO>

    @Query("SELECT * FROM devices WHERE model LIKE '%' || :search || '%'")
    fun observeSearchDevice(search: String): Flow<List<DeviceDAO>>

    @Query("SELECT * FROM devices WHERE is_favorite = 1")
    fun observeAllFavorites(): Flow<List<DeviceDAO>>

    @Query("SELECT * FROM devices")
    fun observeAll(): Flow<List<DeviceDAO>>

    @Query("SELECT COUNT(id) FROM devices")
    suspend fun getCount(): Int

    @Query("SELECT * FROM devices WHERE id =:id")
    fun observe(id: Int): Flow<DeviceDAO?>

    @Query("DELETE FROM devices")
    suspend fun clear()

    @Query("UPDATE devices SET is_favorite = :isFavorite WHERE id=:id")
    suspend fun setFavorite(id: Int, isFavorite: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(devices: List<DeviceDAO>)
}