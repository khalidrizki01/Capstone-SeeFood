package com.example.capstone_seefood

import android.os.Parcel
import android.os.Parcelable

data class PaymentID(
    val paymentID: String?, val date: String?, val total: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paymentID)
        parcel.writeString(date)
        parcel.writeString(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentID> {
        override fun createFromParcel(parcel: Parcel): PaymentID {
            return PaymentID(parcel)
        }

        override fun newArray(size: Int): Array<PaymentID?> {
            return arrayOfNulls(size)
        }
    }
}

