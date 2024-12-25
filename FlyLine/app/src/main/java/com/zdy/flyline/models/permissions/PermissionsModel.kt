package com.zdy.flyline.models.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsModel(

) {

    private var requestPermissionsLauncher : ActivityResultLauncher<Array<String>>? = null

    init {

    }

    fun setRequestPermissionLauncher(launcher: ActivityResultLauncher<Array<String>>?){
        requestPermissionsLauncher = launcher
    }

    fun requestPermissions(context: Context){

    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun hasNearbyDevicesPermission(context: Context): Boolean {
        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                        Manifest.permission.BLUETOOTH_CONNECT)== PackageManager.PERMISSION_GRANTED
        }else{
            true
        }
    }

    fun hasPostNotifications(context: Context) : Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            // Для устройств ниже Android 13 разрешение на уведомления не требуется
            true
        }
    }


    fun hasLocationPermission(context: Context): Boolean {
        try{
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } catch (_: Exception){
            return false
        }
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
////
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