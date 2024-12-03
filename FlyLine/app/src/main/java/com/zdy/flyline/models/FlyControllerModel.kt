package com.zdy.flyline.models

import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel

// model with more useful parameters of FlyController
class FlyControllerModel(
     private val bluetoothSendingModel: BleSendingModel
) {


    fun getTimer(){
        bluetoothSendingModel.get("EWT"){
            if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                try{
                    timer = it.data?.toInt()
                } catch (ex: Exception){

                }

            }
        }
    }

    public var timer: Int? = 0








}