package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class ObserveSearchDevices(private val devicesRepository: DevicesRepository) {
    operator fun invoke(query: String) = devicesRepository.observeSearchDevices(query)
}
