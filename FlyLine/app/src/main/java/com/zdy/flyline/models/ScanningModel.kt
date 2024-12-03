package com.zdy.flyline.models

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
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
    private val context: Context
) {

    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    val SCAN_PERIOD = 2000

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

}