package com.crysure.reflvy.data

data class NotifyChat(
    val chat : String,
    val from: String,
    val iconBot : Boolean,
    val time : Boolean,
    val timeString : String,
    val clickable : Boolean,
    val eventClick : String,
    var ditanggapi : Boolean,
    var index : Int
) {
    companion object {
        val notifChat = mutableListOf<NotifyChat>()
        var notify : Boolean = false
    }
}
