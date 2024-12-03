package com.zdy.flyline.BLE.Repository

import com.zdy.flyline.utils.connectionState

interface IConnectionState {
    fun onConnectionStateChanged(state: connectionState)
}