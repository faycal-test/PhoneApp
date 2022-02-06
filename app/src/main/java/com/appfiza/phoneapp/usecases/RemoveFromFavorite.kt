package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class RemoveFromFavorite(private val devicesRepository: DevicesRepository) {
    suspend operator fun invoke(id: Int) = devicesRepository.removeFromFavorite(id)
}