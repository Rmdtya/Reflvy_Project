package com.example.reflvy
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reflvy.data.News
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.data.Screening
import com.example.reflvy.data.YoutubeVideo
import com.example.reflvy.utils.NonScrollWebView
import com.example.reflvy.utils.YouTubeApiService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class VideoActivity : AppCompatActivity() {

    companion object{
        val listDetails = mutableListOf<VideoItem>()
    }

    private lateinit var webView: WebView
    private lateinit var customViewContainer: FrameLayout
    private lateinit var footer : RelativeLayout
    private lateinit var iconMenu : ImageView
    private lateinit var iconSetting : ImageView
    private lateinit var iconBot : ImageView
    private var originalOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private val API_KEY = "AIzaSyDJ91qC7p0kEpuQlI9ZkpDGohC1vQWj9C8"
    private var view : Int = 0


    private val db = Firebase.firestore
    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater


    private lateinit var textTitle : TextView
    private lateinit var textChannel : TextView
    private lateinit var textView : TextView

    private lateinit var thumbnail : ImageView

    private lateinit var youtubeApiService: YouTubeApiService

    private lateinit var currentVideoId : String
    private var currentIndex : Int = 0

    val VIDEO_ID = "6Z6o5VlyLHQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        Footer()

        textTitle = findViewById(R.id.titleVideo)
        textChannel = findViewById(R.id.channel)
        textView = findViewById(R.id.view)

        linearContainer = findViewById(R.id.video_container)
        inflater = LayoutInflater.from(this)

        webView = findViewById(R.id.webView)
        webView.setBackgroundColor(Color.TRANSPARENT)

        customViewContainer = findViewById<FrameLayout>(R.id.customView)

        Toast.makeText(this, YoutubeVideo.videoList.size.toString(), Toast.LENGTH_SHORT).show()

        currentVideoId = intent.getStringExtra("url") ?: "6Z6o5VlyLHQ"
        currentIndex = intent.getIntExtra("index", 0)

        SetWebView(currentVideoId)


        GlobalScope.launch(Dispatchers.Main) {
            val videoId = "fB8TyLTD7EE"
            val videoDetails = fetchVideoDetails(currentVideoId)
            if (videoDetails != null) {
                textTitle.text = videoDetails.title
                textChannel.text = videoDetails.channel
                textView.text = videoDetails.viewCount

                // ... dan seterusnya
            } else {
                // Tangani jika tidak ada data atau terjadi error
            }

            fetchVideoDetailsForMultipleIds()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        youtubeApiService = retrofit.create(YouTubeApiService::class.java)

    }

    private fun SetWebView(url : String){
        val video =
            "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$url\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"

        val settings: WebSettings = webView.settings
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.loadWithOverviewMode = true
        webView.loadData(video, "text/html", "utf-8")
        webView.settings.javaScriptEnabled = true

        try {
            webView.webChromeClient = object : WebChromeClient() {
                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)
                    originalOrientation = requestedOrientation
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE)
                    val handler = Handler()
                    handler.postDelayed({
                        customViewContainer.addView(view)
                        customViewContainer.visibility = View.VISIBLE
                        webView.visibility = View.GONE
                        footer.visibility = View.GONE
                        iconMenu.visibility = View.GONE
                        iconSetting.visibility = View.GONE
                        iconBot.visibility = View.GONE

                        try {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        }catch (e: Exception){

                        }
                    }, 200) // Delay in milliseconds (0.5 seconds)

                    customViewContainer.setOnTouchListener { _, event ->
                        // Hide the status bar again
                        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE)
                        false
                    }
                }

                override fun onHideCustomView() {
                    super.onHideCustomView()
                    customViewContainer.removeAllViews()
                    customViewContainer.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                    footer.visibility = View.VISIBLE
                    iconMenu.visibility = View.VISIBLE
                    iconSetting.visibility = View.VISIBLE
                    iconBot.visibility = View.VISIBLE
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

                    try {
                        requestedOrientation = originalOrientation
                    }catch (e: Exception){

                    }
                }
            }
        }catch (e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun fetchVideoDetails(videoId: String): VideoDetails? {
        try {
            val response = youtubeApiService.getVideoDetails(API_KEY, "snippet,statistics", videoId)
            if (response.isSuccessful) {
                val videoItems = response.body()?.items
                if (!videoItems.isNullOrEmpty()) {
                    val videoItem = videoItems[0]
                    val title = videoItem.snippet.title
                    val viewCount = videoItem.statistics.viewCount
                    val channelId = videoItem.snippet.channelTitle
                    val publishedAt = parsePublishedAt(videoItem.snippet.publishedAt)
                    val thumbnailUrl = videoItem.snippet.thumbnails.high.url
                    val totalView = formatViews(viewCount)

                    // Membuat objek VideoDetails
                    val videoDetails = VideoDetails(
                        title = title,
                        channel = channelId,
                        viewCount = totalView,
                        publishedAt = publishedAt,
                        thumbnailUrl = thumbnailUrl
                    )

                    return videoDetails
                }
            }
        } catch (e: Exception) {
            // Tangani error jika terjadi
            e.printStackTrace()
        }

        return null // Jika terjadi error atau tidak ada data yang ditemukan
    }

    suspend fun fetchVideoDetailsForMultipleIds(): List<VideoItem> {

        try {
            for (video in YoutubeVideo.videoList) {
                val videoDetails = fetchVideoDetails(video.url)

                if (videoDetails != null) {
                    ShowVideo(video.videoID, video.url, videoDetails.title, videoDetails.channel, videoDetails.viewCount, videoDetails.publishedAt, videoDetails.thumbnailUrl)
                }


//                if (videoDetails != null) {
//                    val videoItem = VideoItem(
//                        title = videoDetails.title,
//                        channel = videoDetails.channel,
//                        viewCount = videoDetails.viewCount,
//                        publishedAt = videoDetails.publishedAt,
//                        thumbnailUrl = videoDetails.thumbnailUrl
//                    )
//                    listDetails.add(videoItem)
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return listDetails
    }


    fun formatViews(viewCount: String): String {
        val views = viewCount.toLongOrNull()
        return views?.let {
            when {
                views >= 1000000 -> {
                    val millionViews = views / 1000000
                    "${millionViews}jt"
                }
                views >= 1000 -> {
                    val thousandViews = views / 1000
                    "${thousandViews} k"
                }
                else -> viewCount
            }
        } ?: viewCount // Jika gagal mengonversi, kembalikan nilai asli
    }


    fun ShowVideo(id: Int, url : String, titleValue : String, channelValue: String, viewValue : String, publishValue : String, thumbnailValue : String) {

            val template: View = inflater.inflate(R.layout.template_video, null)
            linearContainer.addView(template)

            val image: ImageView = template.findViewById(R.id.img)
            val title: TextView = template.findViewById(R.id.title)
            val channel: TextView = template.findViewById(R.id.published)
            val view: TextView = template.findViewById(R.id.view)
            val upload: TextView = template.findViewById(R.id.upload)


            title.text = titleValue
            channel.text = channelValue
            view.text = viewValue
            upload.text = publishValue

            Glide.with(this)
                .load(thumbnailValue)
                .into(image)


            template.setOnClickListener {
                val intent = Intent(this@VideoActivity, VideoActivity::class.java)
                // Kirimkan index i dan playlistID sebagai parameter ke PlayerActivity
                intent.putExtra("url", url)
                intent.putExtra("index", id)
                // Mulai aktivitas PlayerActivity
                startActivity(intent)
                finish()
            }
    }

    private fun parsePublishedAt(publishedAt: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = formatter.parse(publishedAt)

        val currentTime = System.currentTimeMillis()
        val diffInMillis = currentTime - date.time
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

        if (diffInDays in 1..6) {
            return "$diffInDays - 6 days ago"
        } else if (diffInDays in 7..29) {
            val diffInWeeks = (diffInDays / 7).toInt()
            return "$diffInWeeks week${if (diffInWeeks > 1) "s" else ""} ago"
        } else if (diffInDays in 30..364) {
            val diffInMonths = (diffInDays / 30).toInt()
            return "$diffInMonths month${if (diffInMonths > 1) "s" else ""} ago"
        } else {
            val diffInYears = (diffInDays / 365).toInt()
            return "$diffInYears year${if (diffInYears > 1) "s" else ""} ago"
        }
    }

    fun ClearVideo() {
        linearContainer.removeAllViews()
    }

    data class VideoDetails(
        val title: String,
        val channel: String,
        val viewCount: String,
        val publishedAt: String,
        val thumbnailUrl: String
    )

    data class VideoItem(
        val title: String,
        val channel: String,
        val viewCount: String,
        val publishedAt: String,
        val thumbnailUrl: String
    )

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

}
