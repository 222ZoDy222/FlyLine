package com.zdy.flyline.activities.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zdy.flyline.BLE.Repository.bluetoothModels.BluetoothModel
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import com.zdy.flyline.activities.settings.interfaces.ISettingsActivity
import com.zdy.flyline.activities.settings.viewModel.SettingsViewModel
import com.zdy.flyline.databinding.ActivitySettingsBinding
import com.zdy.flyline.utils.extensions.getParcelableBluetoothDevice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), ISettingsActivity, INavigationActivity {


    private val settingsViewModel : SettingsViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var binding : ActivitySettingsBinding

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.apply {
            // -------------------- Setup Actionbar -------------------- //
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            navController = navHostFragment.navController

            appBarConfiguration = AppBarConfiguration(
                // timerFragment show burger-menu
                setOf(R.id.timerFragment),
                drawerLayout
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            NavigationUI.setupWithNavController(navigationView, navController)

            navigationView.setNavigationItemSelectedListener {
                if(it.itemId == R.id.navigate_connection){
                    // If click button "connection" -> we go back to connection activity
                    settingsViewModel.disconnect()
                    finish()
                } else{
                    // If click another buttons we use automatically navigation by ID's
                    // in nav_graph and side_bar_menu
                    NavigationUI.onNavDestinationSelected(it,navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                true
            }



            // ---------------------------------------------------------- //
        }





    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        settingsViewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.resume(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsViewModel.disconnect()
    }

    companion object{
        const val DEVICE_TAG = "BLE_DEVICE"
    }

    override fun getBtModel(): BluetoothModel? = settingsViewModel.getBtModel()
    override fun getConnectionState() = settingsViewModel.isConnected()
    override fun getNavController(): NavController = navController
}

