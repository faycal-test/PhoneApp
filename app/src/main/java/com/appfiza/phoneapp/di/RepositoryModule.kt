package com.appfiza.phoneapp.di

import com.appfiza.phoneapp.data.DevicesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { DevicesRepository(get(), get(), get(), get()) }
}
