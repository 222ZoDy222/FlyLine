package com.zdy.flyline.protocol.parameters

import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import com.zdy.flyline.BLE.Repository.bluetoothModels.BleSendingModel

data class ParameterInt (
    override val name: String,
    val min: Int,
    val max: Int,
    val default : Int,
    val description: String,
    val command: String?,
    /// 1 - default Int; 2 - Time Int
    val inputType: Int = 1,
) : Parameter(name){
    var currentValue: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt()
    ) {
        currentValue = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(name)
        parcel.writeInt(min)
        parcel.writeInt(max)
        parcel.writeInt(default)
        parcel.writeString(description)
        parcel.writeString(command)
        parcel.writeValue(currentValue)
    }

    override fun describeContents(): Int {
        return 0
    }


    fun getValue(bluetoothSendingModel : BleSendingModel, callback :(() -> Unit)?){
        if(command != null){
            bluetoothSendingModel.get(command){
                if(it.answer == BleSendingModel.FlyControllerAnswer.OK){
                    val valueString = it.data
                    if (valueString != null) {
                        currentValue = valueString.toInt()
                    }
                }
                callback?.invoke()

            }
        } else{
            callback?.invoke()
        }
    }


    fun setValue(bluetoothSendingModel: BleSendingModel, value: Int, callback: (() -> Unit)?){
        if(command != null){
            bluetoothSendingModel.send(command + value){
                if(it.answer == BleSendingModel.FlyControllerAnswer.OK) currentValue = value
                callback?.invoke()
            }
        } else{
            callback?.invoke()
        }
    }


    companion object CREATOR : Parcelable.Creator<ParameterInt> {
        override fun createFromParcel(parcel: Parcel): ParameterInt {
            return ParameterInt(parcel)
        }

        override fun newArray(size: Int): Array<ParameterInt?> {
            return arrayOfNulls(size)
        }
    }
}
