package com.crysure.reflvy.data

import com.crysure.reflvy.R

data class DataCard(
    val index : Int,
    var title : String,
    val cardTitle : List<String> = emptyList(),
    val cardImage : List<Int> = emptyList(),
    val cardDeskripsi : List<String> = emptyList(),
    val cardRarity : List<Int> = emptyList(),
    var cardUnlocked : List<Boolean> = emptyList()
){
    companion object{

        val dataCard = mutableListOf<DataCard>()

        init {
            val collection1 = DataCard(
                0,
                "Dampak Pornografi 1",
                listOf("Kecemasan", "Peningkatan hasrat seksual", "Perasaan bersalah", "Ketergantungan"),
                listOf(R.drawable.card_img1, R.drawable.card_img1, R.drawable.card_img1, R.drawable.card_img1),
                listOf("Terlalu banyak eksposur dapat berkontribusi pada gangguan mental seperti kecemasan.",
                        "Konsumsi pornografi dapat meningkatkan hasrat seksual sesaat, namun efek ini bersifat sementara dan berfluktuasi.",
                        "Beberapa individu merasa bersalah atau malu setelah konsumsi pornografi.",
                        "Meningkatnya konsumsi dapat menyebabkan ketergantungan dan menghabiskan banyak waktu."),
                listOf(1, 2, 3, 4),
                listOf(false, false, false, false)
            )

            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
            dataCard.add(collection1)
        }
    }
}
