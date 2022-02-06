package com.appfiza.phoneapp.data.local.mappers

import com.appfiza.phoneapp.data.local.model.DeviceDAO
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.util.DAOMapper

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceDAOMapper(private val specsDAOMapper: SpecsDAOMapper) : DAOMapper<Device, DeviceDAO> {
    override fun mapToDAO(domain: Device) = DeviceDAO(
        id = domain.id,
        manufacturer = domain.manufacturer,
        isFavorite = domain.isFavorite,
        model = domain.model,
        image = domain.image,
        price = domain.price,
        description = domain.description,
        specs = specsDAOMapper.mapToDAO(domain.specs),
        stock = domain.stock
    )

    override fun mapToDomain(dao: DeviceDAO) = Device(
        id = dao.id,
        manufacturer = dao.manufacturer,
        isFavorite = dao.isFavorite,
        model = dao.model,
        image = dao.image,
        price = dao.price,
        description = dao.description,
        specs = specsDAOMapper.mapToDomain(dao.specs),
        stock = dao.stock
    )


}