package com.appfiza.phoneapp.ui.devices_list

import androidx.lifecycle.*
import com.appfiza.phoneapp.DataState
import com.appfiza.phoneapp.test.EspressoIdlingResource
import com.appfiza.phoneapp.usecases.GetDevices
import com.appfiza.phoneapp.usecases.GetDevicesCount
import com.appfiza.phoneapp.usecases.ObserveDevices
import com.appfiza.phoneapp.usecases.ObserveSearchDevices
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceListViewModel(
    private val getDevices: GetDevices,
    private val _observeDevices: ObserveDevices,
    private val _observeSearchDevices: ObserveSearchDevices,
    private val getDevicesCount: GetDevicesCount
) : ViewModel() {

    private val _emptyViewLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val emptyViewLiveData: LiveData<Boolean> = _emptyViewLiveData

    private val _errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveData: LiveData<Boolean> = _errorLiveData

    private val _loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun observeDevices() = _observeDevices().onEach {
        if (it.isEmpty()) _emptyViewLiveData.postValue(true)
        else _emptyViewLiveData.postValue(false)
        // If there is an error no need to display empty view
        errorLiveData.value?.let { error ->
            if (error) _emptyViewLiveData.postValue(false)
        }
    }.asLiveData()

    fun observeSearchDevices(query: String) = _observeSearchDevices(query).onEach {
        if (it.isEmpty()) _emptyViewLiveData.postValue(true)
        else _emptyViewLiveData.postValue(false)
    }.asLiveData()

    fun onClickRetryButton() {
        _errorLiveData.value = false
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            EspressoIdlingResource.increment()
            getDevices().onCompletion { EspressoIdlingResource.decrement() }.collect {
                when (it) {
                    is DataState.Success -> {}
                    is DataState.Idle -> {
                        _loadingLiveData.value = false
                    }
                    is DataState.Loading -> {
                        _loadingLiveData.value = true
                    }
                    is DataState.Error -> {
                        val deviceCount = getDevicesCount()
                        _errorLiveData.value = deviceCount == 0
                    }
                    is DataState.Exception -> {
                        val deviceCount = getDevicesCount()
                        _errorLiveData.value = deviceCount == 0
                    }
                }
            }
        }
    }
}