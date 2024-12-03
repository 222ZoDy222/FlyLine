package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_RPM {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                "Обороты",
                listOf(
                    ParameterInt(
                        "Минимальные обороты двигателя",
                        3000,
                        20000,
                        8000,
                        "",
                        "MID"),
                    ParameterInt(
                        "Средние обороты двигателя",
                        3000,
                        20000,
                        9000,
                        "",
                        "FPD"),
                    ParameterInt(
                        "Максимальные обороты двигателя",
                        3000,
                        20000,
                        10000,
                        "",
                        "MAD"),
                    ParameterInt(
                        "Плавный запуск двигателя",
                        1,
                        50,
                        40,
                        "Скорость набора оборотов при старте.( 1 – медленно 50- быстро)",
                        "STR"),
                    ParameterInt(
                        "Плавная остановка двигателя",
                        1,
                        50,
                        40,
                        "Скорость набора оборотов при остановке. .( 1 – медленно 50- быстро)",
                        "STP"),

                    ParameterInt(
                        "PWM минимум",
                        800,
                        1200,
                        1000,
                        "Минимальное значение PWM.",
                        "PIN"),
                    ParameterInt(
                        "PWM максимум",
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