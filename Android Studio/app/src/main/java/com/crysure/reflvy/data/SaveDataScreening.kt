package com.crysure.reflvy.data

data class SaveDataScreening(
    val pertanyaan: Int,
    val historyObrolan: List<String> = emptyList(),
    val fromBot: List<Boolean> = emptyList(),
    val textTime: List<Boolean> = emptyList(),
    val getTime: List<String> = emptyList(),
    val icon: List<Boolean> = emptyList(),
    val nilaiTerakir : Int
)
{
    companion object {
        val lastData = mutableListOf<SaveDataScreening>()
        val lastDataTiga = mutableListOf<SaveDataScreening>()
    }
}