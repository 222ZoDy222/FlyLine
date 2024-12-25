package com.zdy.flyline.utils.extensions

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment

fun Fragment.vibratePhone() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator ?: return

    if (Build.VERSION.SDK_INT >= 26) {
        var waveTimings = longArrayOf(0,500,100,500,100,500)
        var amplitudes = intArrayOf(0,255,0,255,0,255)
        vibrator.vibrate(VibrationEffect.createWaveform(waveTimings, amplitudes , -1))
    } else {
        // Паттерн для устаревшей версии
        val pattern = longArrayOf(0, 200, 100, 200)
        vibrator.vibrate(pattern, -1)
    }
}