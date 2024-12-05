package com.zdy.flyline.BLE

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.zdy.flyline.R
import java.util.UUID


private const val TAG = "BluetoothLeService"
private val SERVICE_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
private val CHARACTERISTIC_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
private const val FOREGROUND_SERVICE_NOTIFICATION_ID=1
private const val CHANNEL_NAME = "Bluetooth LE Service Channel"
private const val CHANNEL_ID = "BluetoothLeServiceChannel"
class BluetoothLeService : Service() {


    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = STATE_DISCONNECTED

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class LocalBinder : Binder() {
        fun getService(): BluetoothLeService {
            return this@BluetoothLeService
        }
    }


    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice, bluetoothAdapter : BluetoothAdapter, context: Context): Boolean {
        try {
            bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback)
            return true
        } catch (exception: IllegalArgumentException) {

            return false
        }
    }

    private var is_services_discovered = false
    public fun isServicesDiscovered(): Boolean = is_services_discovered
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                connectionState = STATE_CONNECTED
                val notification = createNotification()
                startForeground(FOREGROUND_SERVICE_NOTIFICATION_ID, notification)
                broadcastUpdate(ACTION_GATT_CONNECTED)
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                connectionState = STATE_DISCONNECTED
                stopForeground(true)
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                gatt?.getService(SERVICE_UUID)?.getCharacteristic(CHARACTERISTIC_UUID)?.let { characteristic ->
                    enableNotifications(gatt, characteristic)
                }
                is_services_discovered = true
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            characteristic?.let { broadcastUpdate(ACTION_DATA_ANSWER, it) }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }


        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            broadcastUpdate(ACTION_DATA_READ, characteristic)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
        }


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Fly Controller")
            .setContentText("Connected")
            .setSmallIcon(R.drawable.logo2)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }


    @SuppressLint("MissingPermission")
    private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        gatt.setCharacteristicNotification(characteristic, true)
        characteristic.descriptors.forEach { descriptor ->
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)
        intent.putExtra(ANSWER_DATA, characteristic.getStringValue(0))
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, stringValue: String) {
        val intent = Intent(action)
        intent.putExtra(ANSWER_DATA, stringValue)
        sendBroadcast(intent)
    }


    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    @SuppressLint("MissingPermission")
    private fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    @SuppressLint("MissingPermission")
    public fun StopService(){
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    fun disconnect(){
        bluetoothGatt?.disconnect()
    }

    @SuppressLint("MissingPermission")
    public fun WriteCharacteristic(msg: String){

        val characteristic = getCharacteristic()

        if(characteristic != null){

            characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            characteristic.setValue(msg)
            try{

                if(bluetoothGatt?.writeCharacteristic(characteristic) == true){
                    //ConfigurationFragment.getInstance().MakeToast("Characteristic write true")
                } else{
                    broadcastUpdate(ACTION_DATA_ANSWER, "ErrorWrite")
                    //ConfigurationFragment.getInstance().MakeToast("Characteristic write false")
                }

            } catch(ex: Exception){
                broadcastUpdate(ACTION_DATA_ANSWER, "ErrorWrite")
            }

        } else{
            broadcastUpdate(ACTION_DATA_ANSWER, "ErrorWrite")
        }

    }

    private val CHARACTERISTIC_READ_WRITE_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"

    private var m_gattCharacteristic: BluetoothGattCharacteristic? = null
    private fun getCharacteristic() : BluetoothGattCharacteristic? {
        if(m_gattCharacteristic == null){
            for (service in bluetoothGatt?.services!!){
                for(characteristic in service.characteristics){
                    if(characteristic.uuid.equals(UUID.fromString(CHARACTERISTIC_READ_WRITE_UUID))){
                        m_gattCharacteristic = characteristic
                        return m_gattCharacteristic
                    }
                }
            }
        } else{
            return m_gattCharacteristic
        }
        return null
    }


    companion object {
        const val ACTION_GATT_CONNECTED = "com.zdy.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED = "com.zdy.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED = "com.zdy.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_ANSWER = "com.zdy.bluetooth.le.ACTION_DATA_ANSWER"
        const val ACTION_DATA_READ = "com.zdy.bluetooth.le.ACTION_DATA_READ"
        const val ANSWER_DATA = "com.zdy.bluetooth.le.ANSWER_DATA"
        const val READ_DATA = "com.zdy.bluetooth.le.READ_DATA"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2




        @Volatile
        private var instance: BluetoothLeService? = null

        public fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: BluetoothLeService().also { instance = it }
            }
    }

}