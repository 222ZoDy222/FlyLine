package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.protocol.parameters.MenuParameters

class SettingsMenu_main {
    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                "Основное меню",
                listOf(
                    SettingsMenu_Time.getMenu(),
                    SettingsMenu_RPM.getMenu(),
                    SettingsMenu_SensorSettings.getMenu(),
                    SettingsMenu_PID.getMenu(),
                )
            )
        }
    }
}