package com.appfiza.phoneapp.data.remote.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Fayçal KADDOURI 🐈
 */
data class DevicesDTO(
    @SerializedName("devices") val devices: List<DeviceDTO>
)