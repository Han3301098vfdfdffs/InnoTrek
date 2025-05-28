package com.example.innotrek.data

import com.example.innotrek.R
import com.example.innotrek.data.model.Device

class DataDevices{
    fun loadDevices(): List<Device>{
        return listOf(
            Device(R.string.Device_00, R.drawable.device_00_arduino_mega_2560),
            Device(R.string.Device_01, R.drawable.device_01_arduino_uno_nano),
            Device(R.string.Device_02, R.drawable.device_02_arduino_uno_r3),
            Device(R.string.Device_03, R.drawable.device_03_esp32_c3),
            Device(R.string.Device_04, R.drawable.device_04_esp32_s2),
            Device(R.string.Device_05, R.drawable.device_05_esp32_s3),
            Device(R.string.Device_06, R.drawable.device_06_esp32_s3),
            Device(R.string.Device_07, R.drawable.device_07_raspberry_pi_pico)
        )
    }
}
