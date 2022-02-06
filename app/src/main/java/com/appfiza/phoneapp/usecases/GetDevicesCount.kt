package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class GetDevicesCount(private val devicesRepository: DevicesRepository) {
    suspend operator fun invoke() = devicesRepository.getDevicesCount()
}
