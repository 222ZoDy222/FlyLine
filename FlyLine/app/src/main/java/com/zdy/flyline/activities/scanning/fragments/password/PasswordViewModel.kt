package com.zdy.flyline.activities.scanning.fragments.password

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.FlyControllerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val bluetoothModel: BleSendingModel,
    private val controllerModel: FlyControllerModel,
) : ViewModel() {


    private var password : String = ""

    private val error : MutableLiveData<String> = MutableLiveData("")
    fun getError() = error

    private val isAuthenticated : MutableLiveData<Boolean> = MutableLiveData(false)
    fun getIsAuthenticated() = isAuthenticated

    fun setPass(text: String){
        password = text
    }

    fun applyPass(){
        bluetoothModel.get("CPC$password"){

            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                when (it.data) {
                    "OK" -> {
                        controllerModel.controllerPassword = password
                        isAuthenticated.postValue(true)
                        error.postValue("")
                    }
                    "PINError" -> {
                        error.postValue("Не верный пароль")
                    }
                    else -> {
                        error.postValue("Ошибка")
                    }
                }
            } else{
                error.postValue("Ошибка")
            }


        }

    }

    fun tryDefaultPass(callback: (Boolean)->Unit){
        bluetoothModel.get("CPC0000"){

            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                if(it.data == "OK"){
                    controllerModel.controllerPassword = "0000"
                    callback.invoke(true)
                    return@get
                }
            }

            callback.invoke(false)


        }
    }

    fun pause(){
        bluetoothModel.pause()
    }

    fun resume(context: Context) {
        bluetoothModel.resume(context)
    }



    fun disconnect(){
        bluetoothModel.stopConnection()
    }


}