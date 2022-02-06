package com.appfiza.phoneapp.data.remote.api.mappers

import com.appfiza.phoneapp.data.remote.api.model.DeviceDTO
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.util.DomainMapper

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceDTOMapper(private val specsDTOMapper: SpecsDTOMapper) : DomainMapper<DeviceDTO, Device> {
    override fun mapToDomain(entity: DeviceDTO): Device = Device(
        id = entity.id ?: throw MappingException("Device ID cannot be null"),
        description = entity.description.orEmpty(),
        image = entity.image.orEmpty(),
        isFavorite = entity.isFavorite ?: false,
        manufacturer = entity.manufacturer.orEmpty(),
        model = entity.model.orEmpty(),
        price = entity.price.orEmpty(),
        stock = entity.stock ?: 0,
        specs = specsDTOMapper.mapToDomain(entity.specs)
    )
}