package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.R
import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_RPM {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                (R.string.rpm),
                listOf(
                    ParameterInt(
                        (R.string.minimum_rpm),
                        3000,
                        20000,
                        8000,
                        "",
                        "MID"),
                    ParameterInt(
                        (R.string.middle_rpm),
                        3000,
                        20000,
                        9000,
                        "",
                        "FPD"),
                    ParameterInt(
                        (R.string.maximum_rpm),
                        3000,
                        20000,
                        10000,
                        "",
                        "MAD"),
                    ParameterInt(
                        (R.string.smooth_engine_start),
                        1,
                        50,
                        40,
                        "Скорость набора оборотов при старте.( 1 – медленно 50- быстро)",
                        "STR"),
                    ParameterInt(
                        (R.string.Smooth_engine_stop),
                        1,
                        50,
                        40,
                        "Скорость набора оборотов при остановке. .( 1 – медленно 50- быстро)",
                        "STP"),

                    ParameterInt(
                        (R.string.pwm_minimum),
                        800,
                        1200,
                        1000,
                        "Минимальное значение PWM.",
                        "PIN"),
                    ParameterInt(
                        (R.string.pwm_maximum),
                        1800,
                        2400,
                        2000,
                        "Максимальное значение PWM.",
                        "PAX"),
                )
            )
        }
    }


}