package com.zdy.flyline.activities.calibration

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalibrationViewModel @Inject constructor(
    private val sendingModel: BleSendingModel
) : ViewModel() {


    private val isLoading = MutableLiveData<Boolean>(false)
    fun getIsLoading() = isLoading

    private val progressBarValue = MutableLiveData<Float>(0f)
    fun getProgressBarValue() = progressBarValue
    private var isRunning = false
    private var job: Job? = null
    fun actionDownStop(){
        if (isRunning) return
        isRunning = true
        job = viewModelScope.launch {
            while (isRunning) {
                progressBarValue.value = (progressBarValue.value ?: 0f) + 1
                if ((progressBarValue.value ?: 0f) >= 100f) {
                    isRunning = false
                    calibration()
                    break
                }
                delay(10)
            }
        }
    }


    fun actionUpStop(){
        isRunning = false
        job?.cancel()
        progressBarValue.value = 0f
    }

    private fun calibration() {
        isLoading.postValue(true)
        sendingModel.send("CAL"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                onCalibrationComplete?.invoke(true)
            } else {
                onCalibrationComplete?.invoke(false)
            }
            isLoading.postValue(false)
        }
    }

    private var onCalibrationComplete : ((Boolean) -> Unit)? = null
    fun setOnCalibrationComplete(callback: (Boolean) -> Unit){
        onCalibrationComplete = callback
    }

    fun pause(){
        sendingModel.pause()
    }

    fun resume(context: Context) {
        sendingModel.resume(context)
    }
}