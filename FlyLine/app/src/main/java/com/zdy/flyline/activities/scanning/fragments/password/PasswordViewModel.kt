package com.zdy.flyline.activities.scanning.fragments.password

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val bluetoothModel: BleSendingModel
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

    fun pause(){
        bluetoothModel.pause()
    }

    fun resume(context: Context) {
        bluetoothModel.resume(context)
    }

    fun destroy(){
        bluetoothModel.stopConnection()
    }


}