package com.zdy.flyline.models

import android.app.Application
import android.content.Context
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

    var controllerName : String? = null

    private var vibroTime: Int = 0

    fun getVibroTime(context: Context) : Int{
        val sharedPref = context.getSharedPreferences("VibroPref",Context.MODE_PRIVATE)
        if(sharedPref == null){
            vibroTime = 15
            return vibroTime
        }
        vibroTime = sharedPref.getInt(VIBRO_TIME_KEY,15)
        return vibroTime
    }

    fun setVibroTime(context: Context, value: Int){
        val sharedPref = context.getSharedPreferences("VibroPref",Context.MODE_PRIVATE) ?: return
        vibroTime = value
        with(sharedPref.edit()){
            putInt(VIBRO_TIME_KEY,value)
            apply()
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
    private var vibroListener : (()->Unit)? = null
    fun setTickListener(listener: (Int)->Unit){
        tickListener = listener
    }

    fun setVibroListener(listener: () -> Unit){
        vibroListener = listener
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
                if(currentTime == vibroTime) vibroListener?.invoke()
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

    companion object{
        private const val VIBRO_TIME_KEY = "VIBRO_TIME_KEY"
    }



}