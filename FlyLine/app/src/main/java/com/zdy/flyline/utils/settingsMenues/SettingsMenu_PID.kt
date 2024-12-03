package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_PID {

    companion object{
        fun getMenu() : MenuParameters{
            return MenuParameters(
                "Настройки PID",
                listOf(
                    ParameterInt(
                        "ПИД Kp",
                        0,
                        999,
                        40,
                        "",
                        "PKP",
                        1
                    ),
                    ParameterInt(
                        "ПИД Ki",
                        0,
                        999,
                        200,
                        "",
                        "PKI",
                        1
                    ),
                    ParameterInt(
                        "ПИД Kd",
                        0,
                        999,
                        80,
                        "",
                        "PKD"
                    ),
                )
            )
        }
    }
}