package com.appfiza.phoneapp.di

import com.appfiza.phoneapp.usecases.*
import org.koin.dsl.module

/**
 * Created by Fayçal KADDOURI 🐈
 */
val useCasesModule = module {
    factory { ObserveFavoriteDevices(get()) }
    factory { GetDevices(get()) }
    factory { ObserveDevices(get()) }
    factory { GetDevicesCount(get()) }
    factory { GetDevice(get()) }
    factory { AddToFavorite(get()) }
    factory { ObserveSearchDevices(get()) }
    factory { RemoveFromFavorite(get()) }
}