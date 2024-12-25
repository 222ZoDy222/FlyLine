package com.zdy.flyline.models

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData

class ScanningModel(
    val context: Context
) {

    private var mScanning = false
    private var scanning : Boolean
        get(){
            return mScanning
        }
        set(value){
            mScanning = value
            onScanningListener?.invoke(mScanning)
        }

    private var onScanningListener: ((Boolean)->Unit)? = null
    fun setOnScanningListener(callback: ((Boolean)->Unit)){
        onScanningListener = callback
    }
    fun getIsScanning() = scanning


    private val handler = Handler(Looper.getMainLooper())
    private val SCAN_PERIOD = 2500

    private var _locationEnabled :Boolean = false
    val locationEnabled get() = _locationEnabled

    private var _bluetoothEnabled :Boolean = false
    val bluetoothEnabled get() = _bluetoothEnabled

    private val bluetoothAdapter: BluetoothAdapter
        get(){
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            return bluetoothManager.adapter
        }
    private val bluetoothLeScanner: BluetoothLeScanner
        get() {
            return bluetoothAdapter.bluetoothLeScanner
        }


    private fun isLocationEnabled(): Boolean {
        val locationManager = this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    @SuppressLint("MissingPermission")
    fun startScan(callback: ScanCallback) {

        if (!scanning) {
            _bluetoothEnabled = true
            _locationEnabled = true
            if (!bluetoothAdapter.isEnabled) {
                // Bluetooth is not enabled, prompt the user to turn it on
                // TODO: Turn on bluetooth
                _bluetoothEnabled = false
                return
            } else if(!isLocationEnabled()){
                // Location is not enabled, prompt the user to turn it on
                _locationEnabled = false
                return
            }
            else {
                // Bluetooth is enabled, start scanning
                _locationEnabled = true
                _bluetoothEnabled = true
                handler.postDelayed({
                    try{
                        bluetoothLeScanner.stopScan(callback)
                    } catch (e: Exception) {
                        return@postDelayed
                    }
                    scanning = false
                }, SCAN_PERIOD.toLong())

                scanning = true
                bluetoothLeScanner.startScan(callback)

            }
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(callback)

        }
    }

    fun createScanFilter(): ScanFilter {
        return ScanFilter.Builder()
            .setDeviceName("FLT-") // Установим префикс имени устройства
            .build()
    }

    fun createScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // Режим сканирования
            .build()
    }

}