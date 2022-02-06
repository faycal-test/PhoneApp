package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class ObserveFavoriteDevices(private val devicesRepository: DevicesRepository) {
    operator fun invoke() = devicesRepository.observeFavoritesDevices()
}