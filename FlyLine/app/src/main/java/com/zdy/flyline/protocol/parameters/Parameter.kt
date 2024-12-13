package com.zdy.flyline.protocol.parameters

import android.os.Parcel
import android.os.Parcelable

open class Parameter(
    open val name: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parameter> {
        override fun createFromParcel(parcel: Parcel): Parameter {
            return Parameter(parcel)
        }

        override fun newArray(size: Int): Array<Parameter?> {
            return arrayOfNulls(size)
        }
    }
}
