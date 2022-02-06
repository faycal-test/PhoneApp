package com.appfiza.phoneapp.data.remote.api.mappers

import com.appfiza.phoneapp.data.remote.api.model.SpecsDTO
import com.appfiza.phoneapp.model.Specs
import com.appfiza.phoneapp.util.DomainMapper

/**
 * Created by Fayçal KADDOURI 🐈
 */
class SpecsDTOMapper : DomainMapper<SpecsDTO?, Specs> {
    override fun mapToDomain(entity: SpecsDTO?): Specs = Specs(
        body = entity?.body.orEmpty(),
        memory = entity?.memory.orEmpty(),
        battery = entity?.memory.orEmpty(),
        chipset = entity?.chipset.orEmpty(),
        display = entity?.display.orEmpty(),
        platform = entity?.platform.orEmpty()
    )
}