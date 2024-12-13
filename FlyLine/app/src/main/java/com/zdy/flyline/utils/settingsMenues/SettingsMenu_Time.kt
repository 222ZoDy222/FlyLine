package com.zdy.flyline.utils.settingsMenues

import com.zdy.flyline.R
import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

class SettingsMenu_Time {

    companion object{
        fun getMenu() : MenuParameters {
            return MenuParameters(
                (R.string.time),
                listOf(
                    ParameterInt(
                        (R.string.Time_before_start),
                        0,
                        60,
                        5,
                        "от 1 до 60 секунд",
                        "TBS",
                        2),
                    ParameterInt(
                        (R.string.fly_time),
                        0,
                        420,
                        330,
                        "Время работы мотора без учета разгона и остановки",
                        "EWT",
                        2),
                    ParameterInt(
                        (R.string.signal_interval),
                        0,
                        10,
                        2,
                        "от 0 до 10 секунд. Интервал подачи двойного сигнала до начала работы двигателя (5 – подавать сигнал каждые 5 секунд до запуска двигателя),  0 – функция отключена",
                        "BST",
                        2),
                    ParameterInt(
                        (R.string.Vibrate_signal_until_the_finish),
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