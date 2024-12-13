package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.R
import com.zdy.flyline.protocol.parameters.MenuParameters

class SettingsMenu_main {
    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                (R.string.main_menu),
                listOf(
                    SettingsMenu_Time.getMenu(),
                    SettingsMenu_RPM.getMenu(),
                    SettingsMenu_SensorSettings.getMenu(),
                    SettingsMenu_PID.getMenu(),
                    MenuParameters(R.string.change_password, listOf(), 1),
                    MenuParameters(R.string.calibration, listOf(), 2)
                )
            )
        }
    }
}