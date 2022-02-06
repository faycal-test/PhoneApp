package com.appfiza.phoneapp.ui.devices_favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appfiza.phoneapp.MainCoroutinesRule
import com.appfiza.phoneapp.data.DevicesRepository
import com.appfiza.phoneapp.data.local.daos.DevicesDao
import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.local.mappers.SpecsDAOMapper
import com.appfiza.phoneapp.data.local.model.DeviceDAO
import com.appfiza.phoneapp.data.remote.api.DevicesService
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.SpecsDTOMapper
import com.appfiza.phoneapp.usecases.ObserveFavoriteDevices
import com.appfiza.phoneapp.utils.MockUtil.mockDevice
import com.appfiza.phoneapp.utils.getOrAwaitValue
import com.appfiza.phoneapp.utils.toCache
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Fayçal KADDOURI 🐈
 */

@ExperimentalCoroutinesApi
class MyFavouritesDevicesViewModelTest {

    private lateinit var repository: DevicesRepository
    private lateinit var viewModel: MyFavouritesDevicesViewModel
    private lateinit var observeFavoritesDevices: ObserveFavoriteDevices
    private val devicesDao: DevicesDao = mock()
    private val deviceDTOMapper: DeviceDTOMapper = DeviceDTOMapper(SpecsDTOMapper())
    private val deviceDAOMapper: DeviceDAOMapper = DeviceDAOMapper(SpecsDAOMapper())
    private val service: DevicesService = mock()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        repository = DevicesRepository(service, devicesDao, deviceDTOMapper, deviceDAOMapper)
        observeFavoritesDevices = ObserveFavoriteDevices(repository)
        viewModel = MyFavouritesDevicesViewModel(observeFavoritesDevices)
    }

    @Test
    fun `Load devices from favorites and no devices available should update empty state to true`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            whenever(devicesDao.observeAllFavorites()).thenReturn(devicesFlow)

            viewModel.observeDevices().getOrAwaitValue()

            val result = viewModel.emptyViewLiveData.getOrAwaitValue()
            assertThat(result, `is`(true))
        }

    @Test
    fun `Load devices from favorites and there is devices available should update empty state to false`() =
        runBlocking {
            val devicesFlow = flow { emit(listOf(mockDevice(1).toCache())) }
            whenever(devicesDao.observeAllFavorites()).thenReturn(devicesFlow)

            viewModel.observeDevices().getOrAwaitValue()

            val result = viewModel.emptyViewLiveData.getOrAwaitValue()
            assertThat(result, `is`(false))
        }
}