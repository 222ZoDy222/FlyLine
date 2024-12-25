package com.zdy.flyline.activities.settings.viewModel

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.IConnectionState
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleConnectionModel
import com.zdy.flyline.utils.connectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bluetoothConnectionModel: BleConnectionModel,
) : ViewModel() {

    fun getBtModel() = bluetoothConnectionModel

    private val mIsConnected: MutableLiveData<connectionState> = MutableLiveData(connectionState.disconnected)


    val goConnectionList : MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        bluetoothConnectionModel.setOnConnectionStateChanged(object : IConnectionState{
            override fun onConnectionStateChanged(state: connectionState) {
                mIsConnected.postValue(state)
                if(state == connectionState.disconnected){
                    goConnectionList.postValue(true)
                }
            }
        })
    }

    fun connect(btDevice: BluetoothDevice, context: Context){

        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        if(adapter != null){

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
        if(!bluetoothConnectionModel.getConnectionState()){
            goConnectionList.postValue(true)
        }
    }

    fun disconnect(){
        bluetoothConnectionModel.stopConnection()
    }
}