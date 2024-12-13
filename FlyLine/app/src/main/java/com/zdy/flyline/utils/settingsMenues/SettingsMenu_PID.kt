package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.R
import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_PID {

    companion object{
        fun getMenu() : MenuParameters{
            return MenuParameters(
                (R.string.pid_settings),
                listOf(
                    ParameterInt(
                        (R.string.kp_PID),
                        0,
                        999,
                        40,
                        "",
                        "PKP",
                        1
                    ),
                    ParameterInt(
                        (R.string.ki_PID),
                        0,
                        999,
                        200,
                        "",
                        "PKI",
                        1
                    ),
                    ParameterInt(
                        (R.string.kd_PID),
                        0,
                        999,
                        80,
                        "",
                        "PKD"
                    ),
                ),
                5,
            )
        }
    }
}