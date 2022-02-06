package com.appfiza.phoneapp.ui.device_details

import android.content.Context
import androidx.lifecycle.*
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.usecases.AddToFavorite
import com.appfiza.phoneapp.usecases.GetDevice
import com.appfiza.phoneapp.usecases.RemoveFromFavorite
import com.appfiza.phoneapp.util.Event
import kotlinx.coroutines.launch

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceDetailsViewModel(
    private val deviceID: Int,
    private val getDevice: GetDevice,
    private val addToFavorite: AddToFavorite,
    private val removeFromFavorite: RemoveFromFavorite,
    private val context: Context
) : ViewModel() {

    private val _messageLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    val messageLiveData: LiveData<Event<String>> = _messageLiveData

    val device: LiveData<Device> = getDevice(deviceID).asLiveData()

    fun onClickOnFavorite(device: Device) {
        if (device.isFavorite) {
            viewModelScope.launch { removeFromFavorite(device.id) }
            _messageLiveData.value = Event(context.getString(R.string.device_removed_from_favorites))
        } else {
            viewModelScope.launch { addToFavorite(device.id) }
            _messageLiveData.value = Event(context.getString(R.string.device_added_to_favorites))
        }
    }

}