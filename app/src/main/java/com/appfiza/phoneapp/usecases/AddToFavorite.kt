package com.appfiza.phoneapp.usecases

import com.appfiza.phoneapp.data.DevicesRepository

/**
 * Created by Fayçal KADDOURI 🐈
 */
class AddToFavorite(private val devicesRepository: DevicesRepository) {
    suspend operator fun invoke(id: Int) = devicesRepository.addToFavorite(id)
}