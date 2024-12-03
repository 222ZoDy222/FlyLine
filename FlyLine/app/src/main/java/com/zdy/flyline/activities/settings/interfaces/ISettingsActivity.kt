package com.zdy.flyline.activities.settings.interfaces

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.zdy.flyline.BLE.Repository.bluetoothModels.BluetoothModel
import com.zdy.flyline.utils.connectionState

interface ISettingsActivity {
    fun getBtModel() : BluetoothModel?
    fun getConnectionState() : MutableLiveData<connectionState>

}