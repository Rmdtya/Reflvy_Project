package com.example.reflvy.data

data class DragonConditionData(
    var lastActive : String,
    var speedCoin : Float,
    var coinOvertime : Float,
) {
    companion object {
        var dragonConditionData =
            DragonConditionData("",  2f, 1f)
    }
}
