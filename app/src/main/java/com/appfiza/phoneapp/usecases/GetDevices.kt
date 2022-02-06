package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class GetDevices(private val devicesRepository: DevicesRepository) {
    operator fun invoke() = devicesRepository.fetchDevices()
}