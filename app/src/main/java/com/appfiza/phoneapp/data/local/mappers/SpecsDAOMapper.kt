package com.appfiza.phoneapp.data.local.mappers

import com.appfiza.phoneapp.data.local.model.SpecsDAO
import com.appfiza.phoneapp.model.Specs
import com.appfiza.phoneapp.util.DAOMapper

/**
 * Created by Fayçal KADDOURI 🐈
 */
class SpecsDAOMapper : DAOMapper<Specs, SpecsDAO> {
    override fun mapToDAO(domain: Specs) = SpecsDAO(
        body = domain.body,
        memory = domain.memory,
        battery = domain.battery,
        chipset = domain.chipset,
        display = domain.display,
        platform = domain.platform
    )

    override fun mapToDomain(dao: SpecsDAO) = Specs(
        body = dao.body,
        memory = dao.memory,
        battery = dao.battery,
        chipset = dao.chipset,
        display = dao.display,
        platform = dao.platform
    )
}