package com.example.innotrek.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.innotrek.data.model.Device

class DeviceViewModel : ViewModel() {
    private val _devices = mutableStateListOf<Device>()
    val devices: List<Device> = _devices

    fun addDevice(device: Device) {
        _devices.add(device)
    }
}
