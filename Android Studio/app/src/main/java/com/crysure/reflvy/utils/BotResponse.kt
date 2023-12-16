package com.crysure.reflvy.utils

import com.crysure.reflvy.utils.Constrant.OPEN_GOOGLE
import com.crysure.reflvy.utils.Constrant.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Sup"
                    2 -> "Buongiorno!"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            message.contains("pornografi apa?") -> {
                when (random) {
                    0 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    1 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    2 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    else -> "error"
                }
            }

            message.contains("Apa Itu Pornografi")-> {
                when (random) {
                    0 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    1 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    2 -> "Pornografi adalah penggambaran tubuh manusia atau perilaku seksualitas manusia"
                    else -> "error"
                }
            }

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "idk"
                    1 -> "idk"
                    2 -> "idk"
                    else -> "error"
                }
            }
        }
    }
}