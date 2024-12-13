package com.zdy.flyline.protocol.parameters

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class MenuParameters(
    override val name: Int,
    val parameters: List<Parameter>,
    val activityToStart: Int = 0
) : Parameter(name) {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Parameter)!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(name)
        parcel.writeTypedList(parameters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuParameters> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): MenuParameters {
            return MenuParameters(parcel)
        }

        override fun newArray(size: Int): Array<MenuParameters?> {
            return arrayOfNulls(size)
        }
    }
}
