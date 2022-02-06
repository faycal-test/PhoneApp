package com.appfiza.phoneapp.di

import com.appfiza.phoneapp.data.local.DeviceDatabase
import org.koin.dsl.module

/**
 * Created by Fayçal KADDOURI 🐈
 */
val roomModule = module {
    single { DeviceDatabase.getInstance(get()) }
    single { get<DeviceDatabase>().devicesDao() }
}