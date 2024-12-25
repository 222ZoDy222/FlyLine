package com.zdy.flyline.activities.scanning.scanningFragment.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleConnectionModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.permissions.PermissionsModel
import com.zdy.flyline.R
import com.zdy.flyline.models.ScanningModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanningViewModel @Inject constructor(
    private val bluetoothModel: BleConnectionModel,
) : ViewModel() {



    private val errorMessage: MutableLiveData<Any> = MutableLiveData(null)

    private val isScanning : MutableLiveData<Boolean> = MutableLiveData(false)
    fun getIsScanning() = isScanning



    fun getErrorMessage() = errorMessage

    private lateinit var permissionsModel : PermissionsModel
    private lateinit var scanningModel : ScanningModel



    fun onCreate(context: Context){
        bluetoothModel.stopConnection()
        permissionsModel = PermissionsModel()
        scanningModel = ScanningModel(context)
        scanningModel.setOnScanningListener {
            isScanning.postValue(it)
        }
        checkPermissions(context)

    }

    fun checkPermissions(context: Context){


        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if(permissionsModel.hasLocationPermission(context)){
                    if(permissionsModel.hasNearbyDevicesPermission(context)){
                        errorMessage.postValue(null)
                        startScan()
                    }else{
                        errorMessage.postValue(R.string.hasnearbydevicespermission)
                        permissionsModel.requestNearbyDevicesPermission()
                    }
                }else{
                    errorMessage.postValue(R.string.nolocationpermission)
                    permissionsModel.requestLocationPermission()
                }
            }else {
                if (permissionsModel.hasLocationPermission(context)) {
                    errorMessage.postValue(null)
                    startScan()
                } else {
                    errorMessage.postValue(R.string.nolocationpermission)
                    permissionsModel.requestLocationPermission()
                }
            }
        } catch (ex: Exception){
            var t = ex.localizedMessage
        }

    }


    private val bleDevices : MutableLiveData<MutableList<BluetoothDevice>> = MutableLiveData(
        mutableListOf()
    )

    fun getDevices() = bleDevices

    fun tryScanning(context: Context, permissionLauncher: ActivityResultLauncher<Array<String>>?){
        permissionsModel.setRequestPermissionLauncher(permissionLauncher)
        checkPermissions(context)
    }
    private fun startScan(){
        bleDevices.value?.clear()
        scanningModel.startScan(leScanCallback)
        if(!scanningModel.bluetoothEnabled){
            errorMessage.postValue(R.string.bluetooth_is_disabled)
        } else if(!scanningModel.locationEnabled){
            errorMessage.postValue(R.string.location_is_disabled)
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

                    if(device.name != null && !contains(device) && device.name?.startsWith("FLT-") == true) {
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

    fun resume(){
        isScanning.postValue(scanningModel.getIsScanning())
    }



}