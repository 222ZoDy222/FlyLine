package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.R
import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_SensorSettings {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                (R.string.Sensor_parameters),
                listOf(
                    ParameterInt(
                        (R.string.Operating_mode),
                        0,
                        1,
                        1,
                        "Режим работы",
                        "MOD"),
                    ParameterInt(
                        (R.string.SAS_sensor_area),
                        1,
                        50,
                        15,
                        "1 – минимальная  чувствительность,  50 – максимальная чувствительность",
                        "SAS"),
                    ParameterInt(
                        (R.string.SIY_sensor_area),
                        1,
                        200,
                        50,
                        "1 – минимальная  чувствительность,  200 – максимальная чувствительность",
                        "SIY"),
                )
            )
        }
    }


}