package com.zdy.flyline.activities.scanning.fragments.connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.IConnectionState
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.FlyControllerModel
import com.zdy.flyline.utils.connectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val bluetoothModel: BleSendingModel,
    private val flyControllerModel: FlyControllerModel
) : ViewModel() {

    private val mIsConnected: MutableLiveData<connectionState> = MutableLiveData(connectionState.disconnected)
    fun isConnected() = mIsConnected

    @SuppressLint("MissingPermission")
    fun connect(btDevice: BluetoothDevice, context: Context){

        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if(adapter != null){
            bluetoothModel.setOnConnectionStateChanged(object : IConnectionState {
                override fun onConnectionStateChanged(state: connectionState) {
                    mIsConnected.postValue(state)
                }
            })
            flyControllerModel.controllerName = btDevice.name
            bluetoothModel.startConnecting(btDevice, context)

        } else{
            // TODO: Throw bluetooth adapter is incorrect
        }
    }



    fun pause(){
        bluetoothModel.pause()
    }

    fun resume(context: Context) {
        bluetoothModel.resume(context)
    }

    fun disconnect(){
        bluetoothModel.stopConnection()
    }
}