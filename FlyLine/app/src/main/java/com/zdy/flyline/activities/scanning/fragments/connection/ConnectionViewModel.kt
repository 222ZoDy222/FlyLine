package com.zdy.flyline.activities.scanning.fragments.connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.IConnectionState
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleConnectionModel
import com.zdy.flyline.protocol.parameters.Parameter
import com.zdy.flyline.utils.connectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val bluetoothConnectionModel: BleConnectionModel,
) : ViewModel() {

    private val mIsConnected: MutableLiveData<connectionState> = MutableLiveData(connectionState.disconnected)
    fun isConnected() = mIsConnected

    fun connect(btDevice: BluetoothDevice, context: Context){

        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if(adapter != null){
            bluetoothConnectionModel.setOnConnectionStateChanged(object : IConnectionState {
                override fun onConnectionStateChanged(state: connectionState) {
                    mIsConnected.postValue(state)
                }
            })

            bluetoothConnectionModel.startConnecting(btDevice, context)

        } else{
            // TODO: Throw bluetooth adapter is incorrect
        }
    }

    fun pause(){
        bluetoothConnectionModel.pause()
    }

    fun resume(context: Context) {
        bluetoothConnectionModel.resume(context)
    }

    fun destroy(){
        bluetoothConnectionModel.stopConnection()
    }
}