package com.appfiza.phoneapp.utils

import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.local.mappers.SpecsDAOMapper
import com.appfiza.phoneapp.data.local.model.DeviceDAO
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.SpecsDTOMapper
import com.appfiza.phoneapp.data.remote.api.model.DeviceDTO
import com.appfiza.phoneapp.data.remote.api.model.SpecsDTO
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.model.Specs

/**
 * Created by Fayçal KADDOURI 🐈
 */


object MockUtil {


    fun mockDevice(id: Int, model: String = "Some model", isFavorite: Boolean = false) = DeviceDTO(
        id = id,
        manufacturer = "Some manufacturer",
        isFavorite = isFavorite,
        model = model,
        image = "Some image",
        price = "Some price",
        description = "Some description",
        specs = mockSpecs(),
        stock = 1000
    )

    private fun mockSpecs() = SpecsDTO(
        battery = "Some battery",
        body = "Some body",
        chipset = "Some chipset",
        memory = "Some memory",
        display = "Some display",
        platform = "Some platform"
    )




}


fun List<DeviceDAO>.daoToDomain() = this.map { it.toDomain() }

fun List<DeviceDTO>.dtoToDomain() = this.map { it.toDomain() }

fun List<DeviceDTO>.dtoToCache() = this.map { it.toCache() }

fun List<Device>.toCache() = this.map { it.toCache() }

fun List<Device>.toDto() = this.map { it.toCache() }

fun DeviceDTO.toDomain() = DeviceDTOMapper(SpecsDTOMapper()).mapToDomain(this)

fun DeviceDTO.toCache() = DeviceDTOMapper(SpecsDTOMapper()).mapToDomain(this).toCache()

fun Device.toCache() = DeviceDAOMapper(SpecsDAOMapper()).mapToDAO(this)

fun DeviceDAO.toDomain() = DeviceDAOMapper(SpecsDAOMapper()).mapToDomain(this)
