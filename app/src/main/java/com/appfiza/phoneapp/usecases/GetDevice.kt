package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class GetDevice(private val devicesRepository: DevicesRepository) {
    operator fun invoke(id: Int) = devicesRepository.getDevice(id)
}