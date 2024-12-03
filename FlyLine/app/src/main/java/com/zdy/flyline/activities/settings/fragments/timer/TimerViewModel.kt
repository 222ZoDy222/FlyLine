package com.zdy.flyline.activities.settings.fragments.timer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.FlyControllerModel
import com.zdy.flyline.utils.connectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val bluetoothSendingModel: BleSendingModel,

) : ViewModel() {

    private val flyControllerModel = FlyControllerModel(bluetoothSendingModel)
    val version : MutableLiveData<String> = MutableLiveData("")

    val rpmMid : MutableLiveData<Int> = MutableLiveData()


    val flyTime : MutableLiveData<Int> = MutableLiveData(null)

    val sensorSAS : MutableLiveData<Int> = MutableLiveData(null)
    val sensorSIY : MutableLiveData<Int> = MutableLiveData(null)
    val mode : MutableLiveData<Int> = MutableLiveData(null)
    val voltage : MutableLiveData<Int> = MutableLiveData(null)
    val rpmMin : MutableLiveData<Int> = MutableLiveData(null)
    val rpmMax : MutableLiveData<Int> = MutableLiveData(null)
    val errorMessage : MutableLiveData<String> = MutableLiveData(null)

    init {
        if(bluetoothSendingModel.getConnectionState() != connectionState.disconnected) {
            requestErrors()
        }
    }


    private fun requestErrors() = viewModelScope.launch {

        while (true){
            delay(5000)
            bluetoothSendingModel.get("CHE"){
                if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                    when(it.data){
                        "OK" -> {
                            errorMessage.postValue("")
                        }
                        "Error01" -> {
                            errorMessage.postValue("Ошибка датчика (сенсора)")
                        }
                        "Error02" -> {
                            errorMessage.postValue("Ошибка контроля оборотов")
                        }
                        "Error03" -> {
                            errorMessage.postValue("Ошибка заданных оборотов")
                        }

                    }
                }

            }
        }

    }

    fun reloadParameters(){
        if(bluetoothSendingModel.getConnectionState() != connectionState.disconnected) {
            requestParameters()
            flyControllerModel.getTimer()
        }
    }
    private fun requestParameters() = viewModelScope.launch{

        requestParameter(version,"VER")
        requestParameterInt(sensorSAS,"SAS")
        requestParameterInt(sensorSIY,"SIY")
        requestParameterInt(mode,"MOD")
        //requestParameter(voltage,"")
        requestParameterInt(rpmMin,"MID")
        requestParameterInt(rpmMid,"FPD")
        requestParameterInt(rpmMax,"MAD")

    }

    private suspend fun requestParameter(liveData: MutableLiveData<String>, command : String){
        bluetoothSendingModel.get(command){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                liveData.postValue(it.data)
            } else{
                viewModelScope.launch {
                    delay(2000)
                    requestParameter(liveData,command)
                }
            }
        }
    }

    private suspend fun requestParameterInt(liveData: MutableLiveData<Int>, command : String){
        bluetoothSendingModel.get(command){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                liveData.postValue(it.data?.toInt())
            } else{
                viewModelScope.launch {
                    delay(2000)
                    requestParameterInt(liveData,command)
                }
            }
        }
    }

    fun engineStop(){
        bluetoothSendingModel.send("STOP"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){

            } else {
                // TODO: Error
            }
        }
    }


    fun minusRPM(){
        if(rpmMid.value != null && rpmMin.value != null){
            if(rpmMid.value!! - RPM_COUNTER > rpmMin.value!!){
                sendRequest(rpmMid,"FPD", rpmMid.value!!.minus(RPM_COUNTER))
            }
        }

    }

    fun plusRPM(){
        if(rpmMid.value != null && rpmMax.value != null){
            if(rpmMid.value!! + RPM_COUNTER < rpmMax.value!!){
                sendRequest(rpmMid,"FPD", rpmMid.value!!.plus(RPM_COUNTER))
            }
        }

    }

    fun sendRequest(liveData: MutableLiveData<Int>, command : String, value: Int){
        bluetoothSendingModel.send(command + value.toString()){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                liveData.postValue(value)
            }
        }
    }


    companion object{
        private val RPM_COUNTER = 50
    }
}
