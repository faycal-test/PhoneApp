package com.appfiza.phoneapp.data.remote.api

import com.appfiza.phoneapp.data.remote.api.model.DevicesDTO
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

/**
 * Created by Fayçal KADDOURI 🐈
 */
interface DevicesService {

    @GET(ApiConstants.DEVICES_ENDPOINT)
    suspend fun fetchDevices(): ApiResponse<DevicesDTO>
}