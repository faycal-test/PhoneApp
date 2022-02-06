package com.appfiza.phoneapp.data

import app.cash.turbine.test
import com.appfiza.phoneapp.DataState
import com.appfiza.phoneapp.MainCoroutinesRule
import com.appfiza.phoneapp.data.local.daos.DevicesDao
import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.local.mappers.SpecsDAOMapper
import com.appfiza.phoneapp.data.remote.api.DevicesService
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.SpecsDTOMapper
import com.appfiza.phoneapp.data.remote.api.model.DevicesDTO
import com.appfiza.phoneapp.utils.MockUtil.mockDevice
import com.appfiza.phoneapp.utils.dtoToCache
import com.appfiza.phoneapp.utils.dtoToDomain
import com.appfiza.phoneapp.utils.toCache
import com.appfiza.phoneapp.utils.toDomain
import com.nhaarman.mockitokotlin2.*
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.ExperimentalTime

/**
 * Created by Fayçal KADDOURI 🐈
 */
@ExperimentalTime
@ExperimentalCoroutinesApi
class DevicesRepositoryTest {

    private lateinit var repository: DevicesRepository

    private val service: DevicesService = mock()
    private val devicesDao: DevicesDao = mock()
    private val deviceDTOMapper: DeviceDTOMapper = DeviceDTOMapper(SpecsDTOMapper())
    private val deviceDAOMapper: DeviceDAOMapper = DeviceDAOMapper(SpecsDAOMapper())

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        repository =
            DevicesRepository(service, devicesDao, deviceDTOMapper, deviceDAOMapper, Dispatchers.IO)
    }

    @Test
    fun addToFavoriteTest() = runBlocking {
        repository.addToFavorite(1)

        verify(devicesDao, atLeastOnce()).setFavorite(1, 1)
    }

    @Test
    fun removeFromFavoriteTest() = runBlocking {
        repository.removeFromFavorite(1)

        verify(devicesDao, atLeastOnce()).setFavorite(1, 0)
    }

    @Test
    fun getDevicesCountTest() = runBlocking {
        val expectedResult = 3
        whenever(devicesDao.getCount()).thenReturn(expectedResult)

        val result = repository.getDevicesCount()

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun observeFavoritesDeviceTest() = runBlocking {
        val device = mockDevice(1, isFavorite = true)
        val devicesFlow = flow { emit(listOf(device.toCache())) }
        whenever(devicesDao.observeAllFavorites()).thenReturn(devicesFlow)

        val result = repository.observeFavoritesDevices().first()

        assertThat(result, `is`(listOf(device.toDomain())))
    }

    @Test
    fun observeDevicesTest() = runBlocking {
        val device = mockDevice(1)
        val devicesFlow = flow { emit(listOf(device.toCache())) }
        whenever(devicesDao.observeAll()).thenReturn(devicesFlow)

        val result = repository.observeDevices().first()

        assertThat(result, `is`(listOf(device.toDomain())))
    }

    @Test
    fun observeSearchDevicesTest() = runBlocking {
        val device = mockDevice(1)
        val devicesFlow = flow { emit(listOf(device.toCache())) }
        whenever(devicesDao.observeSearchDevice(any())).thenReturn(devicesFlow)

        val result = repository.observeSearchDevices("app").first()

        assertThat(result, `is`(listOf(device.toDomain())))
    }

    @Test
    fun getDeviceTest() = runBlocking {
        val device = mockDevice(1)
        val deviceFlow = flow { emit(device.toCache()) }
        whenever(devicesDao.observe(1)).thenReturn(deviceFlow)

        val result = repository.getDevice(1).first()

        assertThat(result, `is`(device.toDomain()))
    }

    @Test
    fun fetchDevicesTest() = runBlocking {
        val mockData =
            DevicesDTO(
                listOf(
                    mockDevice(1, isFavorite = true),
                    mockDevice(2),
                    mockDevice(3),
                    mockDevice(4, isFavorite = true)
                )
            )
        whenever(service.fetchDevices()).thenReturn(ApiResponse.of { Response.success(mockData) })
        whenever(devicesDao.getAllFavourites()).thenReturn(
            listOf(
                (mockDevice(
                    1,
                    isFavorite = false
                )).toCache(),
                (mockDevice(
                    2,
                    isFavorite = true
                )).toCache()
            )
        )
        whenever(devicesDao.getAll()).thenReturn(mockData.devices.dtoToCache())

        repository.fetchDevices().test {
            assertThat(awaitItem(), `is`(DataState.Loading))
            val expectedItem = awaitItem()

            assertThat(expectedItem, `is`(DataState.Success(mockData.devices.dtoToDomain())))
            val expectedResponse = (expectedItem as DataState.Success).data
            assertThat(expectedResponse[0].id, `is`(1))
            assertThat(expectedResponse[0].model, `is`("Some model"))
            assertThat(expectedResponse[0].manufacturer, `is`("Some manufacturer"))
            // Ensure that our first ans last device is still in favorites
            assertThat(expectedResponse[0].isFavorite, `is`(true))
            assertThat(expectedResponse[3].isFavorite, `is`(true))
            assertThat(awaitItem(), `is`(DataState.Idle))

            awaitComplete()
        }

        verify(devicesDao, atLeastOnce()).insertAll(any())
        verify(service, atLeastOnce()).fetchDevices()
        verify(devicesDao, atLeastOnce()).getAllFavourites()
        verify(devicesDao, atLeastOnce()).clear()
        verify(devicesDao, atLeastOnce()).getAll()
        verifyNoMoreInteractions(service)
    }

}