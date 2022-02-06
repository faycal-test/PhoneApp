package com.appfiza.phoneapp.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.appfiza.phoneapp.data.local.DeviceDatabase
import com.appfiza.phoneapp.utils.MockUtil.mockDevice
import com.appfiza.phoneapp.utils.toCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Fayçal KADDOURI 🐈
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
@Suppress("IllegalIdentifier")
class DevicesDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: DeviceDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DeviceDatabase::class.java
        ).build()
    }

    @After
    fun destroy() {
        database.close()
    }

    @Test
    fun `insert devices and getAll should return all devices`() = runBlockingTest {
        //GIVEN
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN - get all devices from the database
        val loaded = database.devicesDao().getAll()

        //THEN -
        assertThat(loaded, `is`(deviceList))
    }

    @Test
    fun `insert devices and getAllFavorites should return list of favorites`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache(),
            mockDevice(3, isFavorite = true).toCache(),
            mockDevice(4, isFavorite = true).toCache(),
            mockDevice(5).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        val favoritesList = listOf(
            mockDevice(3, isFavorite = true).toCache(),
            mockDevice(4, isFavorite = true).toCache()
        )

        //WHEN
        val loaded = database.devicesDao().getAllFavourites()

        //THEN
        assertThat(loaded, `is`(favoritesList))
    }

    @Test
    fun `insert device and observeAllFavorites should emit list of favorites`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache(),
            mockDevice(3, isFavorite = true).toCache(),
            mockDevice(4, isFavorite = true).toCache(),
            mockDevice(5).toCache()
        )

        val favoritesList = listOf(
            mockDevice(3, isFavorite = true).toCache(),
            mockDevice(4, isFavorite = true).toCache()
        )

        database.devicesDao().insertAll(deviceList)

        //WHEN
        val firstIEmission = database.devicesDao().observeAllFavorites().first()

        //THEN - the emitted data contains all devices
        assertThat(firstIEmission, `is`(favoritesList))
    }

    @Test
    fun `insert device and observe all should emit them all`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN - observe all  flow emits a value
        val firstIEmission = database.devicesDao().observeAll().first()

        //THEN - the emitted data contains all devices
        assertThat(firstIEmission, `is`(deviceList))
    }

    @Test
    fun `insert device and observe search device should return first one`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1, model = "apple").toCache(),
            mockDevice(2, model = "Google").toCache(),
            mockDevice(2, model = "1VALET Phone").toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN
        val firstIEmission = database.devicesDao().observeSearchDevice("app").first()

        val expectedList = listOf(
            mockDevice(1, model = "apple").toCache()
        )

        //THEN - the emitted data contains all devices with the letters "app"
        assertThat(firstIEmission, `is`(expectedList))
    }

    @Test
    fun `insert device and get count should return 2`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN get count
        val count = database.devicesDao().getCount()

        //THEN The count must be 2
        assertThat(count, `is`(deviceList.size))
    }


    @Test
    fun `insert devices and clear should return empty list`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN
        database.devicesDao().clear()
        val devices = database.devicesDao().getAll()

        //THEN The list should be empty
        assertThat(devices, `is`(listOf()))
    }

    @Test
    fun `insert devices and set favorite to true first device favorite should be true`() = runBlockingTest {
        //GIVEN - insert devices
        val deviceList = listOf(
            mockDevice(1).toCache(),
            mockDevice(2).toCache()
        )
        database.devicesDao().insertAll(deviceList)

        //WHEN
        database.devicesDao().setFavorite(1, 1)
        val devices = database.devicesDao().getAll()

        //THEN The device favorite should be true
        assertThat(devices[0].isFavorite, `is`(true))
    }

    @Test
    fun `insert device and observe by ID should emit device`() = runBlockingTest {
        //GIVEN
        val device = mockDevice(1).toCache()
        val deviceList = listOf(device)
        database.devicesDao().insertAll(deviceList)

        //WHEN
        val firstIEmission = database.devicesDao().observe(device.id).first()

        //THEN
        assertThat(firstIEmission, `is`(device))
    }


}