package com.example.reflvy.model

import android.os.Parcel
import android.os.Parcelable

data class Message(
    val message : String,
    val sentBy : String,
    val timeStamp : String
){
    companion object {
        const val SENT_BY_ME = "sent_me"
        const val SENT_BY_BOT = "sent_bot"
    }
}