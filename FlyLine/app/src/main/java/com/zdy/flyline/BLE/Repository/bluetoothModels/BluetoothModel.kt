package com.zdy.flyline.BLE.Repository.bluetoothModels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import com.zdy.flyline.BLE.BluetoothLeService
import com.zdy.flyline.BLE.Repository.IConnectionState
import com.zdy.flyline.utils.connectionState

open class BluetoothModel() {

    protected lateinit var context: Context

    protected lateinit var bluetoothManager : BluetoothManager

    protected lateinit var bluetoothAdapter : BluetoothAdapter

    protected var bluetoothService : BluetoothLeService? = null

    fun getConnectionState() : Boolean{
        return if(bluetoothService != null){
            bluetoothService!!.getConnectionState() == BluetoothLeService.STATE_CONNECTED
        } else{
            false
        }
    }


    protected fun makeGattUpdateIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
            addAction(BluetoothLeService.ACTION_DATA_ANSWER)
            addAction(BluetoothLeService.ACTION_DATA_READ)
        }
    }










}