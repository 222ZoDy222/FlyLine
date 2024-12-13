package com.zdy.flyline.models.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class PermissionsModel(
    private val context: Context
) {

    private var requestPermissionsLauncher : ActivityResultLauncher<Array<String>>? = null

    fun setRequestPermissionLauncher(launcher: ActivityResultLauncher<Array<String>>){
        requestPermissionsLauncher = launcher
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = this.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun hasNearbyDevicesPermission(): Boolean {
        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.S) {
            this.context.let {
                ContextCompat.checkSelfPermission(it.applicationContext, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(it.applicationContext,
                            Manifest.permission.BLUETOOTH_CONNECT)== PackageManager.PERMISSION_GRANTED
            }
        }else{
            true
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

//    val requestPermissionLauncher =
//        this.registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission is granted. Continue the action or workflow in your
//                // app.
//            } else {
//                // Explain to the user that the feature is unavailable because the
//                // feature requires a permission that the user has denied. At the
//                // same time, respect the user's decision. Don't link to system
//                // settings in an effort to convince the user to change their
//                // decision.
//            }
//        }
//
//
//    @SuppressLint("LongLogTag")
//    val requestPermissionsLauncher =
//        this.registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { result: Map<String, Boolean> ->{
//
//            if (result.containsValue(false)){
//                Log.d("PERMISSIONS FlyController","Permissions denied")
//            } else{
//                Log.d("PERMISSIONS FlyController","Permissions granted")
//            }
//        }
//
//        }


    @RequiresApi(Build.VERSION_CODES.S)
    fun requestNearbyDevicesPermission(){
        requestPermissionsLauncher?.launch(
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        )
    }
    fun requestLocationPermission() {

        requestPermissionsLauncher?.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


}