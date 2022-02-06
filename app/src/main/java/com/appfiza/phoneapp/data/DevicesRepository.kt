package com.appfiza.phoneapp.data

import androidx.annotation.WorkerThread
import com.appfiza.phoneapp.DataState
import com.appfiza.phoneapp.data.local.daos.DevicesDao
import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.remote.api.DevicesService
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DevicesRepository(
    private val devicesService: DevicesService,
    private val devicesDao: DevicesDao,
    private val deviceDTOMapper: DeviceDTOMapper,
    private val deviceDAOMapper: DeviceDAOMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun addToFavorite(id: Int) {
        withContext(ioDispatcher) {
            devicesDao.setFavorite(id, 1)
        }
    }

    suspend fun removeFromFavorite(id: Int) {
        withContext(ioDispatcher) {
            devicesDao.setFavorite(id, 0)
        }
    }

    suspend fun getDevicesCount() = devicesDao.getCount()

    fun observeFavoritesDevices() = devicesDao.observeAllFavorites()
        .map { list -> list.map { deviceDAOMapper.mapToDomain(it) } }

    fun observeDevices() =
        devicesDao.observeAll().map { list -> list.map { deviceDAOMapper.mapToDomain(it) } }

    fun observeSearchDevices(query: String) = devicesDao.observeSearchDevice(query)
        .map { list -> list.map { deviceDAOMapper.mapToDomain(it) } }

    fun getDevice(id: Int) =
        devicesDao.observe(id).filterNotNull().map { deviceDAOMapper.mapToDomain(it) }

    @WorkerThread
    fun fetchDevices() = flow {
        devicesService.fetchDevices()
            .suspendOnSuccess {
                val devicesDTO = data.devices
                val devicesDTOToStore = devicesDTO.map { deviceDTOMapper.mapToDomain(it) }

                val favouritesDevicesDAO = devicesDao.getAllFavourites()
                val devicesDAOToStore = devicesDTOToStore.map { deviceDAOMapper.mapToDAO(it) }

                /**
                 * Set isFavorite to true for devices that are inside favouritesDevicesDAO
                 */
                devicesDAOToStore.filter { daoToStore ->
                    favouritesDevicesDAO.any { it.id == daoToStore.id }
                }.forEach { it.isFavorite = true }

                devicesDao.clear()
                devicesDao.insertAll(devicesDAOToStore)

                val devicesDAO = devicesDao.getAll()
                val devices = devicesDAO.map { deviceDAOMapper.mapToDomain(it) }

                emit(DataState.Success(devices))
            }
            .suspendOnError { emit(DataState.Error) }
            .suspendOnException { emit(DataState.Exception) }
    }.onStart { emit(DataState.Loading) }.onCompletion { emit(DataState.Idle) }
        .flowOn(ioDispatcher)


}