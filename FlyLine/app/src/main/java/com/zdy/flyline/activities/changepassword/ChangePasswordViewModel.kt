package com.zdy.flyline.activities.changepassword

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel
import com.zdy.flyline.models.FlyControllerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val controllerModel: FlyControllerModel,
    private val sendingModel: BleSendingModel
) : ViewModel() {
    private val errorMessage = MutableLiveData<String>(null)
    fun getErrorMessage() = errorMessage
    private val isComplete = MutableLiveData<Boolean>(false)

    private val shouldCheckOldPassword = MutableLiveData<Boolean>(true)
    fun getShouldCheckOldPassword() = shouldCheckOldPassword
    init {
        if(controllerModel.controllerPassword == "0000"){
            shouldCheckOldPassword.postValue(false)
        } else{
            shouldCheckOldPassword.postValue(true)
        }
    }

    fun sendPassword(
        oldPass: String,
        newPass: String,
        callback: (Boolean)->Unit
    ){
        if(controllerModel.controllerPassword != "0000"){
            errorMessage.postValue(null)
            if(oldPass != controllerModel.controllerPassword){
                errorMessage.postValue("Старый пароль не совпадает")
                callback.invoke(false)
                return
            }
        }

        if(newPass.length != 4){
            errorMessage.postValue("Новый пароль должен содержать только 4 цифры")
            callback.invoke(false)
            return
        }
        try{
            newPass.toInt()
        } catch (ex:Exception){
            errorMessage.postValue("Новый пароль должен содержать только цифры")
        }

        sendingModel.send("NPC${newPass}"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                callback.invoke(true)
            } else{
                errorMessage.postValue("Ошибка изменения пароля")
                callback.invoke(false)
            }
        }

    }

    fun pause(){
        sendingModel.pause()
    }

    fun resume(context: Context) {
        sendingModel.resume(context)
    }

}