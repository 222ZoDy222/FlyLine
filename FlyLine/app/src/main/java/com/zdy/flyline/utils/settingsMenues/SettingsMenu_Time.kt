package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_Time {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                "Время",
                listOf(
                    ParameterInt(
                        "Время до старта",
                        0,
                        60,
                        5,
                        "от 1 до 60 секунд",
                        "TBS",
                        2),
                    ParameterInt(
                        "Время полета",
                        0,
                        420,
                        330,
                        "Время работы мотора без учета разгона и остановки",
                        "EWT",
                        2),
                    ParameterInt(
                        "Интервал сигнала",
                        0,
                        10,
                        2,
                        "от 0 до 10 секунд. Интервал подачи двойного сигнала до начала работы двигателя (5 – подавать сигнал каждые 5 секунд до запуска двигателя),  0 – функция отключена",
                        "BST",
                        2),
                    ParameterInt(
                        "Сигнал вибро до финиша",
                        0,
                        1,
                        0,
                        "Вибрация телефоном ",
                        null,
                        2),
                )
            )
        }
    }

}