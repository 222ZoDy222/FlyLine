package com.zdy.flyline.utils.extensions

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import com.zdy.flyline.activities.settings.SettingsActivity

fun Bundle?.getParcelableBluetoothDevice(): BluetoothDevice? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this?.getParcelable(SettingsActivity.DEVICE_TAG, BluetoothDevice::class.java)
    } else{
        @Suppress("DEPRECATION")
        return this?.getParcelable<BluetoothDevice>(SettingsActivity.DEVICE_TAG)
    }
}