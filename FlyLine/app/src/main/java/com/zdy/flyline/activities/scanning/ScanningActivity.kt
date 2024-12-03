package com.zdy.flyline.activities.scanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }

    override fun getNavController(): NavController = navController


}