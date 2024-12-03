package com.zdy.flyline.activities.scanning.scanningFragment.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zdy.flyline.models.permissions.PermissionsModel
import com.zdy.flyline.R
import com.zdy.flyline.models.ScanningModel

class ScanningViewModel(
    private val application: Application
) : AndroidViewModel(application) {



    private val errorMessage: MutableLiveData<String> = MutableLiveData(null)

    fun getErrorMessage() = errorMessage

    private val permissionsModel by lazy {
        PermissionsModel(application.applicationContext)
    }

    private val scanningModel = ScanningModel(application.applicationContext)


    init{

        checkPermissions()

    }

    fun checkPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if(permissionsModel.hasLocationPermission()){
                if(permissionsModel.hasNearbyDevicesPermission()){
                    errorMessage.postValue(null)
                    startScan()
                }else{
                    errorMessage.postValue(application.applicationContext.getString(R.string.hasnearbydevicespermission))
                    permissionsModel.requestNearbyDevicesPermission()
                }
            }else{
                errorMessage.postValue(application.applicationContext.getString(R.string.nolocationpermission))
                permissionsModel.requestLocationPermission()
            }
        }else {
            if (permissionsModel.hasLocationPermission()) {
                errorMessage.postValue(null)
            } else {
                errorMessage.postValue(application.applicationContext.getString(R.string.nolocationpermission))
                permissionsModel.requestLocationPermission()
            }
        }
    }


    private val bleDevices : MutableLiveData<MutableList<BluetoothDevice>> = MutableLiveData(
        mutableListOf()
    )

    fun getDevices() = bleDevices

    fun startScan(){
        bleDevices.value?.clear()
        scanningModel.startScan(leScanCallback)
        if(!scanningModel.bluetoothEnabled){
            errorMessage.postValue("Bluetooth is disabled")
        } else if(!scanningModel.locationEnabled){
            errorMessage.postValue("Location is disabled")
        } else{
            errorMessage.postValue(null)
        }
    }


    private val leScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let { scanResult ->
                bleDevices.value?.let { list->
                    val device = scanResult.device

                    if(device.name != null && !contains(device)) {
                        list.add(device)
                        // For update observer only ^^
                        bleDevices.value = list
                    }
                }

            }
        }
    }

    private fun contains(device: BluetoothDevice) : Boolean {
        bleDevices.value?.forEach{
            if(it.address.equals(device.address)) return true
        }
        return false
    }











    // PERMISSIONS ------------------------------
    fun setRequestPermissionLauncher(launcher: ActivityResultLauncher<Array<String>>){
        // TODO: Set Request permission launcher in fragment
        permissionsModel.setRequestPermissionLauncher(launcher)
    }
    /////////////////////////////////////////////


}