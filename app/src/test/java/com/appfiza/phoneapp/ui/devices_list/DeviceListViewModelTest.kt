package com.appfiza.phoneapp.ui.devices_list

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
import com.appfiza.phoneapp.usecases.GetDevices
import com.appfiza.phoneapp.usecases.GetDevicesCount
import com.appfiza.phoneapp.usecases.ObserveDevices
import com.appfiza.phoneapp.usecases.ObserveSearchDevices
import com.appfiza.phoneapp.utils.MockUtil.mockDevice
import com.appfiza.phoneapp.utils.getOrAwaitValue
import com.appfiza.phoneapp.utils.toCache
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

/**
 * Created by Fayçal KADDOURI 🐈
 */
@ExperimentalCoroutinesApi
class DeviceListViewModelTest {

    private lateinit var repository: DevicesRepository
    private lateinit var viewModel: DeviceListViewModel
    private lateinit var getDevices: GetDevices
    private lateinit var getDevicesCount: GetDevicesCount
    private lateinit var observeSearchDevices: ObserveSearchDevices
    private lateinit var observeDevices: ObserveDevices
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
        getDevices = GetDevices(repository)
        observeDevices = ObserveDevices(repository)
        getDevicesCount = GetDevicesCount(repository)
        observeSearchDevices = ObserveSearchDevices(repository)
        viewModel =
            DeviceListViewModel(getDevices, observeDevices, observeSearchDevices, getDevicesCount)
    }

    @Test
    fun `fetching devices getting an error should update errorLiveData to true when no devices cached`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            whenever(service.fetchDevices()).thenReturn(ApiResponse.of {
                Response.error(
                    401,
                    "{\"message\": \"The FBI is coming...\"}\n".toResponseBody()
                )
            })
            whenever(devicesDao.observeAll()).thenReturn(devicesFlow)
            whenever(devicesDao.getCount()).thenReturn(0)

            viewModel.loadDevices()

            val result = viewModel.errorLiveData.getOrAwaitValue()
            assertThat(result, `is`(true))
        }

    @Test
    fun `fetching devices getting an error should update errorLiveData to false when there devices cached`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            whenever(service.fetchDevices()).thenReturn(ApiResponse.of {
                Response.error(
                    401,
                    "{\"message\": \"The FBI is coming...\"}\n".toResponseBody()
                )
            })
            whenever(devicesDao.observeAll()).thenReturn(devicesFlow)
            whenever(devicesDao.getCount()).thenReturn(1)

            viewModel.loadDevices()

            val result = viewModel.errorLiveData.getOrAwaitValue()
            assertThat(result, `is`(false))
        }


    @Test
    fun `fetching devices getting an exception should update errorLiveData to false when no devices cached`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            val exception = NullPointerException()
            whenever(service.fetchDevices()).thenReturn( ApiResponse.error(exception))
            whenever(devicesDao.observeAll()).thenReturn(devicesFlow)
            whenever(devicesDao.getCount()).thenReturn(0)

            viewModel.loadDevices()

            val result = viewModel.errorLiveData.getOrAwaitValue()
            assertThat(result, `is`(true))
        }


    @Test
    fun `fetching devices getting an exception should update errorLiveData to false when there devices cached`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            val exception = NullPointerException()
            whenever(service.fetchDevices()).thenReturn( ApiResponse.error(exception))
            whenever(devicesDao.observeAll()).thenReturn(devicesFlow)
            whenever(devicesDao.getCount()).thenReturn(1)

            viewModel.loadDevices()

            val result = viewModel.errorLiveData.getOrAwaitValue()
            assertThat(result, `is`(false))
        }

    @Test
    fun `onClickRetryButton should update error state to false`() = runBlocking {
        val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
        whenever(devicesDao.observeAll()).thenReturn(devicesFlow)

        viewModel.onClickRetryButton()

        val result = viewModel.errorLiveData.getOrAwaitValue()
        assertThat(result, `is`(false))
    }

    @Test
    fun `Load devices and no devices available should update empty state to true`() = runBlocking {
        val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
        whenever(devicesDao.observeAll()).thenReturn(devicesFlow)

        viewModel.loadDevices()
        viewModel.observeDevices().getOrAwaitValue()

        val result = viewModel.emptyViewLiveData.getOrAwaitValue()
        assertThat(result, `is`(true))
    }

    @Test
    fun `Search devices and no devices available should update empty state to true`() =
        runBlocking {
            val devicesFlow = flow<List<DeviceDAO>> { emit(listOf()) }
            whenever(devicesDao.observeSearchDevice("app")).thenReturn(devicesFlow)

            viewModel.loadDevices()
            viewModel.observeSearchDevices("app").getOrAwaitValue()

            val result = viewModel.emptyViewLiveData.getOrAwaitValue()
            assertThat(result, `is`(true))
        }

    @Test
    fun `Search devices and devices available should update empty state to false`() = runBlocking {
        val devicesFlow = flow { emit(listOf(mockDevice(1).toCache())) }
        whenever(devicesDao.observeSearchDevice("app")).thenReturn(devicesFlow)

        viewModel.loadDevices()
        viewModel.observeSearchDevices("app").getOrAwaitValue()

        val result = viewModel.emptyViewLiveData.getOrAwaitValue()
        assertThat(result, `is`(false))
    }


    @Test
    fun `Load devices and no devices should return update empty state to false`() = runBlocking {
        val devicesFlow = flow { emit(listOf(mockDevice(1).toCache())) }
        whenever(devicesDao.observeAll()).thenReturn(devicesFlow)

        viewModel.loadDevices()
        viewModel.observeDevices().getOrAwaitValue()

        val result = viewModel.emptyViewLiveData.getOrAwaitValue()
        assertThat(result, `is`(false))
    }
}