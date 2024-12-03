package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_SensorSettings {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                "Параметры датчиков",
                listOf(
                    ParameterInt(
                        "Режим работы",
                        0,
                        1,
                        1,
                        "Режим работы",
                        "MOD"),
                    ParameterInt(
                        "Зона датчика SAS",
                        1,
                        50,
                        15,
                        "1 – минимальная  чувствительность,  50 – максимальная чувствительность",
                        "SAS"),
                    ParameterInt(
                        "Зона датчика SIY",
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