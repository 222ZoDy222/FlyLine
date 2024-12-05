package com.zdy.flyline.models

import android.os.CountDownTimer
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel

// model with more useful parameters of FlyController
class FlyControllerModel(
     private val bluetoothSendingModel: BleSendingModel
) {

    lateinit var controllerPassword: String

    init {

        bluetoothSendingModel.setFlyListener {
            if(it){
                startTimer()
            } else{
                // TODO: Stop
                timer?.cancel()
                tickListener?.invoke(0)
            }
        }


    }
    fun getTimerParameters(){

        checkIsFlying()

        bluetoothSendingModel.get("EWT"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                try{
                    maxTime = if(it.data != null){
                        it.data.toInt()
                    } else{
                        330
                    }

                } catch (ex: Exception){

                }

            }
        }


    }

    private var maxTime: Int = 0
    private var currentTime: Int = 0
    private var timer : CountDownTimer? = null
    private var tickListener : ((Int) -> Unit)? = null
    fun setTickListener(listener: (Int)->Unit){
        tickListener = listener
    }
    private fun startTimer(startTime: Int? = null){

        var timeToWait = 0
        if(startTime != null){
            timeToWait = startTime * 1000
            currentTime = startTime
        }
        else{
            timeToWait = maxTime * 1000
            currentTime = maxTime
        }

        timer?.cancel()

        timer = object: CountDownTimer(timeToWait.toLong(),1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime--
                tickListener?.invoke(currentTime)
            }

            override fun onFinish() {

            }
        }

        (timer as CountDownTimer).start()

    }

    private fun checkIsFlying(){
        bluetoothSendingModel.send("STS"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                if(it.data != null){
                    val intValue = it.data.toInt()
                    if(intValue != 0){
                        bluetoothSendingModel.isFlying = true
                        startTimer(intValue)
                    }
                }
            }
        }
    }

    fun stopTimer(){
        bluetoothSendingModel.send("STOP"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                timer?.cancel()
                tickListener?.invoke(0)
            } else {

            }
        }
    }





}