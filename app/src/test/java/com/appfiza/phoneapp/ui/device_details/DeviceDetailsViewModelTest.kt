package com.appfiza.phoneapp.ui.device_details

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appfiza.phoneapp.MainCoroutinesRule
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.data.DevicesRepository
import com.appfiza.phoneapp.data.local.daos.DevicesDao
import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.local.mappers.SpecsDAOMapper
import com.appfiza.phoneapp.data.remote.api.DevicesService
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.SpecsDTOMapper
import com.appfiza.phoneapp.usecases.AddToFavorite
import com.appfiza.phoneapp.usecases.GetDevice
import com.appfiza.phoneapp.usecases.RemoveFromFavorite
import com.appfiza.phoneapp.utils.MockUtil.mockDevice
import com.appfiza.phoneapp.utils.toCache
import com.appfiza.phoneapp.utils.toDomain
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Fayçal KADDOURI 🐈
 */

@ExperimentalCoroutinesApi
class DeviceDetailsViewModelTest {

    private lateinit var repository: DevicesRepository
    private lateinit var viewModel: DeviceDetailsViewModel
    private lateinit var getDevice: GetDevice
    private lateinit var addToFavorite: AddToFavorite
    private lateinit var removeFromFavorite: RemoveFromFavorite
    private val devicesDao: DevicesDao = mock()
    private val context: Context = mock()
    private val deviceDTOMapper: DeviceDTOMapper = DeviceDTOMapper(SpecsDTOMapper())
    private val deviceDAOMapper: DeviceDAOMapper = DeviceDAOMapper(SpecsDAOMapper())
    private val service: DevicesService = mock()

    companion object {
        private const val DEVICE_ID = 1
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        repository = DevicesRepository(service, devicesDao, deviceDTOMapper, deviceDAOMapper)
        getDevice = GetDevice(repository)
        addToFavorite = AddToFavorite(repository)
        removeFromFavorite = RemoveFromFavorite(repository)
        viewModel =
            DeviceDetailsViewModel(DEVICE_ID, getDevice, addToFavorite, removeFromFavorite, context)
        whenever(context.getString(R.string.device_removed_from_favorites)).thenReturn("Device removed from your favorites")
        whenever(context.getString(R.string.device_added_to_favorites)).thenReturn("Device added to your favorites")
    }

    @Test
    fun `onClickOnFavorite should setFavorite to false if the device is already true and set message to "Device Removed"`() =
        runBlocking {
            val device = mockDevice(DEVICE_ID, isFavorite = true)
            val deviceFlow = flow { emit(device.toCache()) }
            whenever(devicesDao.observe(DEVICE_ID)).thenReturn(deviceFlow)

            viewModel.onClickOnFavorite(device.toDomain())

            val result = viewModel.messageLiveData.value?.peekContent()
            MatcherAssert.assertThat(
                result, `is`("Device removed from your favorites")
            )

            verify(devicesDao).setFavorite(DEVICE_ID, 0)
        }

    @Test
    fun `onClickOnFavorite should setFavorite to true if the device is false and set message to "Device Added"`() =
        runBlocking {
            val device = mockDevice(DEVICE_ID, isFavorite = false)
            val deviceFlow = flow { emit(device.toCache()) }
            whenever(devicesDao.observe(DEVICE_ID)).thenReturn(deviceFlow)

            viewModel.onClickOnFavorite(device.toDomain())

            val result = viewModel.messageLiveData.value?.peekContent()
            MatcherAssert.assertThat(
                result,
                `is`("Device added to your favorites")
            )

            verify(devicesDao).setFavorite(DEVICE_ID, 1)
        }
}