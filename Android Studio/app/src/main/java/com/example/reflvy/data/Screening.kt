package com.example.reflvy.data

data class Screening(
    val pertanyaan : Int,
    val soalbot: List<String> = emptyList(),
    val textJawab: List<String> = emptyList(),
    val nilai: List<Int> = emptyList(),
    val jawabUser: List<String> = emptyList(),
    val event :  List<Int> = emptyList(),
    val eventRespon : List<String> = emptyList()
)
{
    companion object {
        val screenData = mutableListOf<Screening>()
        var jumlahData = Int
    }
}