package com.zdy.flyline.BLE.Repository.bluetoothModels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.zdy.flyline.BLE.BluetoothLeService
import com.zdy.flyline.BLE.Repository.IConnectionState
import com.zdy.flyline.di.App
import com.zdy.flyline.utils.connectionState

open class BleConnectionModel : BluetoothModel() {

    protected var stateConnection : connectionState = connectionState.disconnected

    protected var connectionStateChanged: IConnectionState? = null
    fun setOnConnectionStateChanged(callback: IConnectionState){
        connectionStateChanged = callback
    }

    protected lateinit var deviceToConnect: BluetoothDevice

    protected open val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    stateConnection = connectionState.connected
                    connectionStateChanged?.onConnectionStateChanged(connectionState.connected)
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    connectionStateChanged?.onConnectionStateChanged(connectionState.disconnected)
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    connectionStateChanged?.onConnectionStateChanged(connectionState.services_discovered)
                }
                BluetoothLeService.ACTION_DATA_ANSWER ->{



                }
                BluetoothLeService.ACTION_DATA_READ ->{

                }
            }
        }
    }

    var isServiceConnected = false
    // Code to manage Service lifecycle.
    protected val serviceConnection: ServiceConnection = object : ServiceConnection {



        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()

            bluetoothService?.connect(deviceToConnect, context)
            isServiceConnected = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            //TODO: Service disconnected error and try create new service
            isServiceConnected = false

        }
    }


    fun reconnect(){
        startConnecting(deviceToConnect,context)
    }


    fun startConnecting(device: BluetoothDevice, context: Context){

        setupContext(context)
        deviceToConnect = device
        // Creating service
        resume(context)
        try{
            if(isServiceConnected){
                context.unbindService(serviceConnection)
                isServiceConnected = false
            }
        } catch (ex: Exception){
            isServiceConnected = false
        }

        val intent = Intent(context, BluetoothLeService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

    }

    protected fun setupContext(context: Context){
        this.context = context
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }



    fun pause(){
        context.unregisterReceiver(gattUpdateReceiver)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun resume(context: Context){
        this.context = context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter(), Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        }
    }



    fun stopConnection(){
        try{
            if(isServiceConnected){
                bluetoothService?.disconnect()
                context.unbindService(serviceConnection)
                isServiceConnected = false
            }
        }catch (_: Exception){

        }

    }

}