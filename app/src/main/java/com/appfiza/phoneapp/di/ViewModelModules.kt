package com.appfiza.phoneapp.di

import com.appfiza.phoneapp.ui.device_details.DeviceDetailsViewModel
import com.appfiza.phoneapp.ui.devices_favorites.MyFavouritesDevicesViewModel
import com.appfiza.phoneapp.ui.devices_list.DeviceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MyFavouritesDevicesViewModel(get()) }
    viewModel { DeviceListViewModel(get(), get(), get(), get()) }
    viewModel { params -> DeviceDetailsViewModel(params.get(), get(), get(), get(), get()) }
}
