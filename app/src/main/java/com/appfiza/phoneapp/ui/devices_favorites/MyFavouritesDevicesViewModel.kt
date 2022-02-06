package com.appfiza.phoneapp.ui.devices_favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.appfiza.phoneapp.usecases.ObserveFavoriteDevices
import kotlinx.coroutines.flow.onEach

/**
 * Created by Fayçal KADDOURI 🐈
 */
class MyFavouritesDevicesViewModel(
    private val _observeFavoritesDevices: ObserveFavoriteDevices,
) : ViewModel() {

    private val _emptyViewLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val emptyViewLiveData: LiveData<Boolean> = _emptyViewLiveData

    fun observeDevices() = _observeFavoritesDevices().onEach {
        if (it.isEmpty()) _emptyViewLiveData.postValue(true)
        else _emptyViewLiveData.postValue(false)
    }.asLiveData()

}