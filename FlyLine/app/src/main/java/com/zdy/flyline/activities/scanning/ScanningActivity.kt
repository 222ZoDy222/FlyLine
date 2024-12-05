package com.zdy.flyline.activities.scanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.zdy.flyline.R
import com.zdy.flyline.activities.scanning.interfaces.INavigationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanningActivity : AppCompatActivity(), INavigationActivity {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(navController.currentDestination?.id == R.id.passwordFragment){
                        navController.navigate(R.id.action_passwordFragment_to_scanningFragment)
                    } else{
                        navController.navigateUp()
                    }
                }
            }
        )

    }


    override fun onSupportNavigateUp(): Boolean {
        if(navController.currentDestination?.id == R.id.passwordFragment){
            navController.navigate(R.id.action_passwordFragment_to_scanningFragment)
            return false
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun getNavController(): NavController = navController


}