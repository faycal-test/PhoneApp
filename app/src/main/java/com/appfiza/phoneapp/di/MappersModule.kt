package com.appfiza.phoneapp.di

import com.appfiza.phoneapp.data.local.mappers.DeviceDAOMapper
import com.appfiza.phoneapp.data.local.mappers.SpecsDAOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.DeviceDTOMapper
import com.appfiza.phoneapp.data.remote.api.mappers.SpecsDTOMapper
import org.koin.dsl.module

/**
 * Created by Fayçal KADDOURI 🐈
 */
val mappersModule = module {
    factory { DeviceDTOMapper(get()) }
    factory { SpecsDTOMapper() }
    factory { DeviceDAOMapper(get()) }
    factory { SpecsDAOMapper() }
}