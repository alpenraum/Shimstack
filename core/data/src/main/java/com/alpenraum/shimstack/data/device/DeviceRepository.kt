package com.alpenraum.shimstack.data.device

interface DeviceRepository {
    abstract fun getDeviceId(): String
}