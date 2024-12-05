package com.zdy.flyline.BLE.Repository.bluetoothModels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import com.zdy.flyline.BLE.BluetoothLeService
import com.zdy.flyline.utils.connectionState
import java.util.LinkedList
import java.util.Queue

class BleSendingModel : BleConnectionModel() {

    override val gattUpdateReceiver: BroadcastReceiver= object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    stateConnection = connectionState.connected
                    connectionStateChanged?.onConnectionStateChanged(connectionState.connected)
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    stateConnection = connectionState.disconnected
                    connectionStateChanged?.onConnectionStateChanged(connectionState.disconnected)
                    reconnect()
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    stateConnection = connectionState.services_discovered
                    connectionStateChanged?.onConnectionStateChanged(connectionState.services_discovered)
                }
                BluetoothLeService.ACTION_DATA_ANSWER ->{
                    var dataString = intent.getStringExtra(BluetoothLeService.ANSWER_DATA)
                    checkReadableData(dataString)


                }
                BluetoothLeService.ACTION_DATA_READ ->{
                    var data = intent.getByteArrayExtra(BluetoothLeService.READ_DATA)
                    var dataString = intent.getStringExtra(BluetoothLeService.ANSWER_DATA)
                    // TODO: Readable data
                    // Make manager for readable data and notifications about readable data
                    Log.i("BLE_READ", "Read data: $dataString")
                }
            }
        }
    }

    var isFlying = false


    private var isFlyingListener : ((Boolean)->Unit)? = null
    fun setFlyListener(callback: (Boolean)->Unit){
        isFlyingListener = callback
    }
    private fun checkReadableData(data: String?){

        when(data){
            "StartFly\n" -> {
                // TODO: Callback start fly
                isFlying = true
                isFlyingListener?.invoke(isFlying)
            }
            "StopFly\n" -> {
                // TODO: callback stop fly
                isFlying = false
                isFlyingListener?.invoke(isFlying)
            }
            else ->{
                endCommand(data)
            }
        }
    }

    private var answers: Queue<(Answer) -> Unit> = LinkedList()

    private var dataToAsk: Queue<String> = LinkedList<String>()

    private var queueBusy = false

    fun save(msg: (Answer) -> Unit){

        dataToAsk.add("SAVE\r\n")
        answers.add(msg)

        nextCommand()
    }

    fun get(value: String, msg: (Answer) -> Unit){
        dataToAsk.add("$value\r\n")
        answers.add(msg)

        nextCommand()
    }

    fun send(value: String, msg : (Answer) -> Unit){

        dataToAsk.add("$value\r\n")
        answers.add(msg)

        nextCommand()

    }


    private val TIME_OUT_TIME : Long = 5000
    val timer = object: CountDownTimer(TIME_OUT_TIME,TIME_OUT_TIME) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            endCommand("TimeOut")
        }
    }

    private fun nextCommand(){


        if(dataToAsk.isNotEmpty() && !queueBusy && canStartSending()){

            queueBusy = true
            val data = dataToAsk.remove()
            LogCharacteristic("Write $data")
            timer.start()
            bluetoothService?.WriteCharacteristic(data);

        }



    }

    private fun endCommand(data: String?){

        timer.cancel()
        if(answers.isEmpty()) {
            queueBusy = false
            return
        }

        LogCharacteristic("End command data: $data")

        var dataAnswer = data
        if(dataAnswer != null) {
            dataAnswer = dataAnswer.replace("\n","").replace("\r","")
        }
        when(dataAnswer){
            "ErrorWrite",
            "Error" ->{
                answers.remove().invoke(Answer(FlyControllerAnswer.ERROR))
            }
            null ->{
                answers.remove().invoke(Answer(FlyControllerAnswer.NULL))
            }
            "TimeOut" ->{
                // TODO: MB we should reconnect
                answers.remove().invoke(Answer(FlyControllerAnswer.TIMEOUT))
            }
            else -> {
                answers.remove().invoke(Answer(FlyControllerAnswer.OK, dataAnswer))
            }
        }

        queueBusy = false
        nextCommand()
    }


    private fun LogCharacteristic(msg: String){
        Log.i("BLE_Controller",msg)
    }


    private fun canStartSending(): Boolean{
        return (bluetoothService != null && bluetoothService!!.isServicesDiscovered())
    }

    enum class FlyControllerAnswer{
        OK,
        ERROR,
        NULL,
        TIMEOUT,
    }

    class Answer(
        val answer: FlyControllerAnswer,
        val data: String? = null
    )
}
