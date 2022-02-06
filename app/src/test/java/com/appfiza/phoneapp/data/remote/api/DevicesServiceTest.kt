package com.appfiza.phoneapp.data.remote.api

import com.appfiza.phoneapp.MainCoroutinesRule
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DevicesServiceTest : ApiAbstract<DevicesService>() {

    private lateinit var service: DevicesService

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun initService() {
        service = createService(DevicesService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun fetchDevicesListFromNetworkTest() = runBlocking {
        enqueueResponse("DevicesResponse.json")
        val response = service.fetchDevices()
        val responseBody = requireNotNull((response as ApiResponse.Success).data)
        mockWebServer.takeRequest()

        assertThat(responseBody.devices[0].model,  `is`("Google Pixel 3"))
        assertThat(responseBody.devices[0].image, `is`("https://the-mobile-store.netlify.app/img/phone_1-min.jpg"))
        assertThat(responseBody.devices[0].price, `is`("799$"))
        assertThat(responseBody.devices[0].manufacturer, `is`("Google Inc."))
        assertThat(responseBody.devices[0].description, `is`("Staying too far from your loved ones? Video call them for hours on end. The weather is romantic? Listen to your favourite playlists all day long. Don’t want to go out this weekend? Then binge watch your favourite series on the Internet. The Pixel 3 ensures that there’s never a dull moment, all thanks to its powerful battery, impressive cameras and its expansive bezel-less display."))
        assertThat(responseBody.devices[0].specs?.chipset, `is`("Qualcomm SDM845 Snapdragon 845 (10 nm)"))
        assertThat(responseBody.devices[0].specs?.memory, `is`("64/128 GB, 4 GB RAM"))
        assertThat(responseBody.devices[0].specs?.platform, `is`("OS Android 9.0 (Pie)"))
    }

}