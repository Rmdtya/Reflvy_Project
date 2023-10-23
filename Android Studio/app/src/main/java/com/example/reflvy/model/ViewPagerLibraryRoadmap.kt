package com.example.reflvy.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reflvy.R
import com.example.reflvy.data.DataCard

class ViewPagerLibraryRoadmap(private var dataCard : List<DataCard>) :
    RecyclerView.Adapter<ViewPagerLibraryRoadmap.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textTitle : TextView = itemView.findViewById(R.id.text_title)

        val card1 : RelativeLayout = itemView.findViewById(R.id.card1)
        val card2 : RelativeLayout = itemView.findViewById(R.id.card2)
        val card3 : RelativeLayout = itemView.findViewById(R.id.card3)
        val card4 : RelativeLayout = itemView.findViewById(R.id.card4)

        val frame1 : FrameLayout = itemView.findViewById(R.id.frame_card1)
        val frame2 : FrameLayout = itemView.findViewById(R.id.frame_card2)
        val frame3 : FrameLayout = itemView.findViewById(R.id.frame_card3)
        val frame4: FrameLayout = itemView.findViewById(R.id.frame_card4)

        val imgCard1 : ImageView = itemView.findViewById(R.id.img_card1)
        val imgCard2 : ImageView = itemView.findViewById(R.id.img_card2)
        val imgCard3 : ImageView = itemView.findViewById(R.id.img_card3)
        val imgCard4 : ImageView = itemView.findViewById(R.id.img_card4)

        val titleCard1 : TextView = itemView.findViewById(R.id.title_card1)
        val titleCard2 : TextView = itemView.findViewById(R.id.title_card2)
        val titleCard3 : TextView = itemView.findViewById(R.id.title_card3)
        val titleCard4 : TextView = itemView.findViewById(R.id.title_card4)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerLibraryRoadmap.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_librarycard, parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerLibraryRoadmap.Pager2ViewHolder, position: Int) {
        val currentDataCard = dataCard[position] // Mendapatkan objek DataCard pada posisi saat ini

        // Mengatur tampilan untuk kartu pertama
        holder.textTitle.text = currentDataCard.title
        holder.titleCard1.text = currentDataCard.cardTitle[0]
        holder.imgCard1.setImageResource(currentDataCard.cardImage[0])

        // Mengatur tampilan untuk kartu kedua
        holder.titleCard2.text = currentDataCard.cardTitle[1]
        holder.imgCard2.setImageResource(currentDataCard.cardImage[1])

        // Mengatur tampilan untuk kartu ketiga
        holder.titleCard3.text = currentDataCard.cardTitle[2]
        holder.imgCard3.setImageResource(currentDataCard.cardImage[2])

        // Mengatur tampilan untuk kartu keempat
        holder.titleCard4.text = currentDataCard.cardTitle[3]
        holder.imgCard4.setImageResource(currentDataCard.cardImage[3])

        SetBgandFrame(holder.card1, holder.frame1, currentDataCard.cardRarity[0])
        SetBgandFrame(holder.card2, holder.frame2, currentDataCard.cardRarity[1])
        SetBgandFrame(holder.card3, holder.frame3, currentDataCard.cardRarity[2])
        SetBgandFrame(holder.card4, holder.frame4, currentDataCard.cardRarity[3])

    }

    private fun SetBgandFrame(bg : RelativeLayout, frame : FrameLayout, rarity : Int){
        if (rarity == 1){
            bg.setBackgroundResource(R.drawable.card_bg1)
            frame.setBackgroundResource(R.drawable.card_frame1)
        }else if(rarity == 2){
            bg.setBackgroundResource(R.drawable.card_bg2)
            frame.setBackgroundResource(R.drawable.card_frame2)
        }else if(rarity == 3){
            bg.setBackgroundResource(R.drawable.card_bg3)
            frame.setBackgroundResource(R.drawable.card_frame3)
        }else if(rarity == 4){
            bg.setBackgroundResource(R.drawable.card_bg4)
            frame.setBackgroundResource(R.drawable.card_frame4)
        }
    }

    override fun getItemCount(): Int {
        return dataCard.size
    }
}