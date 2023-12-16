package com.crysure.reflvy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.crysure.reflvy.data.Music
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.utils.SliderAdapter
import com.crysure.reflvy.utils.SliderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.abs

class PlaylistActivity : AppCompatActivity() {

    lateinit var isViewImage:ViewPager2
    lateinit var isList:ArrayList<SliderModel>
    lateinit var adapter: SliderAdapter
    val sliderHandler = Handler()
    private val db = Firebase.firestore
    lateinit var linearContainer : LinearLayout
    lateinit var inflater: LayoutInflater
    private var isViewStateRestored = false
    val templateList = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        Footer()

        linearContainer = findViewById(R.id.list_music)
        inflater = LayoutInflater.from(this)

        if (savedInstanceState != null) {
            // Jika ada, atur flag isViewStateRestored menjadi true
            isViewStateRestored = true
        }else{
            SetViewPager()
        }
    }

    fun SetViewPager(){

        isViewImage=findViewById(R.id.is_view_image)
        isList= ArrayList()

        adapter= SliderAdapter(isList, this)

        isList.clear()
        isList.add(SliderModel(0, "https://drive.google.com/uc?export=view&id=1a9RusYz-Wfs0cnfuXAGlMa8F18Y1s92q"))
        isList.add(SliderModel(1, "https://images3.alphacoders.com/778/778062.png"))


        adapter.notifyDataSetChanged()

        isViewImage.adapter=adapter
        isViewImage.clipChildren=false
        isViewImage.clipToPadding=false
        isViewImage.offscreenPageLimit=3
        isViewImage.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer= CompositePageTransformer()

        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        compositePageTransformer.addTransformer(object: ViewPager2.PageTransformer{
            override fun transformPage(page: View, position: Float) {

                val r= 1- abs(position)

                page.scaleY=0.85f+r*0.15f
            }
        })

        isViewImage.setPageTransformer(compositePageTransformer)

        isViewImage.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //Looping Picture
//                if(position==isList.size-2){
//                    isViewImage.post(runnable)
//                }

                when(position) {
                    0 -> {
                        ClearPlaylist()
                        ShowMusic(0)

                    }
                    1 -> {
                        ClearPlaylist()
                        ShowMusic(1)
                    }
                    2 -> {
                        ClearPlaylist()
                        ShowMusic(2)
                    }
                    3 -> {
                        ClearPlaylist()
                        ShowMusic(3)
                    }
                }
            }
        })
    }

    fun ShowMusic(indexMusic: Int) {
        ClearPlaylist()

        val textColor1 = ContextCompat.getColor(this, R.color.text_playlist1)
        val textColor2 = ContextCompat.getColor(this, R.color.text_playlist2)

        var jumlahMusic : Int = Music.playList[indexMusic].title.size

        for (music in Music.playList) {
            var indexbg : Int = 1;
            if (music.playlistID == indexMusic) {
                for (i in music.title.indices) {
                    if (indexbg - 1 >= jumlahMusic) {

                    }else{
                        val template: View = inflater.inflate(R.layout.template_music, null)
                        linearContainer.addView(template)

                        val image: ImageView = template.findViewById(R.id.img_cover)
                        val icon: ImageView = template.findViewById(R.id.icon_play)
                        val title: TextView = template.findViewById(R.id.text1)
                        val vocal: TextView = template.findViewById(R.id.text2)
                        val bg: ImageView = template.findViewById(R.id.background)

                        if (indexbg % 2 == 0) {
                            icon.setImageResource(R.drawable.icon_playpoddark)
                            bg.setImageResource(R.drawable.menubg2)
                            title.setTextColor(textColor2)
                            vocal.setTextColor(textColor2)
                        } else {
                            bg.setImageResource(R.drawable.menubg1)
                            title.setTextColor(textColor1)
                            vocal.setTextColor(textColor1)
                        }

                        // Ubah gambar dan teks di setiap template
                        Glide.with(this)
                            .load(music.img[i])
                            .into(image)

                        title.text = music.title[i]
                        vocal.text = music.vocalist[i]

                        template.setOnClickListener {
                            // Buat intent untuk berpindah ke PlayerActivity
                            val intent = Intent(this@PlaylistActivity, PlayerActivity::class.java)
                            // Kirimkan index i dan playlistID sebagai parameter ke PlayerActivity
                            intent.putExtra("INDEX", i)
                            intent.putExtra("PLAYLIST_ID", music.playlistID)
                            // Mulai aktivitas PlayerActivity
                            startActivity(intent)
                        }
                        indexbg++
                    }
                }
            }
        }
    }

    fun ClearPlaylist() {
        linearContainer.removeAllViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Panggil fungsi yang ingin Anda eksekusi saat aplikasi dihancurkan di sini
        ClearPlaylist()
        isList.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Simpan keadaan tampilan activity dengan flag isViewStateRestored
        outState.putBoolean("isViewStateRestored", isViewStateRestored)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Ambil keadaan tampilan activity yang sebelumnya disimpan
        isViewStateRestored = savedInstanceState.getBoolean("isViewStateRestored", false)
    }

    override fun onResume() {
        super.onResume()

        // Jika tampilan activity tidak disimpan sebelumnya, atur ulang tampilan
        if (!isViewStateRestored) {
            ClearPlaylist()
            SetViewPager()
            // Reset flag isViewStateRestored agar dijalankan sekali saja
            isViewStateRestored = true
        }
    }

    private fun Footer(){
        val includedLayout = findViewById<View>(R.id.footer)
        val home: ImageView = includedLayout.findViewById(R.id.home_icon)
        home.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val bot: ImageView = includedLayout.findViewById(R.id.bot_icon)
        bot.setOnClickListener {
            val intent = Intent(this, BotActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val settings: ImageView = includedLayout.findViewById(R.id.setting_icon)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val btn_back : ImageView = findViewById(R.id.icon_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}

