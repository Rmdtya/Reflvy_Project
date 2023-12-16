package com.crysure.reflvy

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.crysure.reflvy.data.Music
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.utils.MusicService


class PlayerActivity : AppCompatActivity() {

    lateinit var audioUrl: String
    private lateinit var seekBar: SeekBar
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    var currentPosition : Int = 0
    var audioDuration : Int = 0
    var waitingTime = 0L
    var isWaiting = false

    private lateinit var handler: Handler
    private var isDelaying = false

    private lateinit var imageCover: ImageView
    private lateinit var title: TextView
    private lateinit var vocal: TextView
    private lateinit var playIcon: ImageView
    private lateinit var pauseIcon : ImageView
    private lateinit var previousIcon: ImageView
    private lateinit var nextIcon: ImageView

    companion object{
        var audioTitle : String =  ""
        var currentIndex : Int = 0
        var playlistID :Int = 0

        fun getPlayerActivityPendingIntent(context: Context, playlistID: Int, indexMusic: Int): PendingIntent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("PLAYLIST_ID", playlistID)
            intent.putExtra("INDEX", indexMusic)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // Tambahkan flag ini untuk menghindari pembuatan instance baru dari PlayerActivity jika sudah berjalan
            return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        Footer()

        imageCover = findViewById(R.id.img_cover)
        title = findViewById(R.id.title)
        vocal = findViewById(R.id.vocal)

        val index = intent.getIntExtra("INDEX", 0)
        playlistID = intent.getIntExtra("PLAYLIST_ID", 0)

        currentIndex = index

        playIcon = findViewById(R.id.icon_play)
        pauseIcon = findViewById(R.id.icon_pause)
        previousIcon = findViewById(R.id.icon_previous)
        nextIcon = findViewById(R.id.icon_next)

        startTime = findViewById(R.id.starttime)
        endTime = findViewById(R.id.endtime)
        seekBar = findViewById(R.id.progres_bar)

        audioUrl = Music.playList[playlistID].src[index]

        if (Music.playList[playlistID].title[index] == MusicService.audioTitle) {
            LoadLayout(playlistID, index)
            updateSeekBar(MusicService.currentPosition, MusicService.musicDuration)
            PlayPauseIcon()
        } else {
            if(MusicService.playStatus){
                MusicService.mediaPlayer?.reset()
                MusicService.mediaPlayer?.stop()
                LoadLayout(playlistID, index)
                PlayMusicService(playlistID, index, audioUrl)
            }else{
                PlayMusicService(playlistID, index, audioUrl)
                LoadLayout(playlistID, index)
            }
            MusicService.playStatus = true
            PlayPauseIcon()
        }

        val intentFilter = IntentFilter("ACTION_UPDATE_SEEK_BAR")
        registerReceiver(seekBarUpdateReceiver, intentFilter)

        val intentStop = IntentFilter("ACTION_STOP_MUSIC")
        registerReceiver(playNextReceiver, intentStop)

        val intentIcon = IntentFilter("PLAY_STATUS")
        registerReceiver(playNextReceiver, intentIcon)

        pauseIcon.setOnClickListener {
            pauseMusic()
        }

        playIcon.setOnClickListener {
            resumeMusic()
        }

        nextIcon.setOnClickListener {
            PlayNextSong()
        }

        previousIcon.setOnClickListener {
            PlayPreviousSong()
        }

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    fun PlayMusicService(playlistID: Int, index: Int, url : String){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("AUDIO_URL", audioUrl)
        intent.putExtra("ID_PLAYLIST", playlistID)
        intent.putExtra("INDEX_AUDIO", index)
        startService(intent)
    }

    fun LoadLayout(playlistID : Int, index : Int){

        Glide.with(this)
            .load(Music.playList[playlistID].cover[index])
            .into(imageCover)

        title.text = Music.playList[playlistID].title[index]
        vocal.text = Music.playList[playlistID].vocalist[index]
    }

    private fun formatTime(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun GetTime(duration: Int): Int {
        var minutes = duration / 1000 / 60
        return minutes
    }

    private val seekBarUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_UPDATE_SEEK_BAR") {
                currentPosition = intent.getIntExtra("CURRENT_POSITION", 0)
                audioDuration = intent.getIntExtra("AUDIO_DURATION", 0)
                updateSeekBar(currentPosition, audioDuration)
            }
        }
    }

    private val playNextReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                when (intent.action) {
                    "ACTION_PLAY_NEXT" -> {
                        val playlistID = intent.getIntExtra("PLAYLIST", 0)
                        val indexMusic = intent.getIntExtra("INDEX", 0)
                        LoadLayout(playlistID, indexMusic)
                    }
                    "ACTION_STOP_MUSIC" -> {
                        stopMusicService()
                    }
                    "PLAY_STATUS" -> {
                        PlayPauseIcon()
                    }
                }
            }
        }
    }

    private fun updateSeekBar(currentPosition: Int, duration: Int) {
        if (duration > 0) {
            seekBar.max = duration // Set the maximum value of the SeekBar to the duration of the song
            seekBar.progress = currentPosition
            startTime.text = formatTime(currentPosition)
            endTime.text = formatTime(duration)
        }
    }

    private fun PlayPauseIcon(){

        if(MusicService.playStatus){
            playIcon.visibility = View.GONE
            pauseIcon.visibility = View.VISIBLE
        }else{
            playIcon.visibility = View.VISIBLE
            pauseIcon.visibility = View.GONE
        }
    }

    private fun pauseMusic() {
        val pauseIntent = Intent(this, MusicService::class.java)
        pauseIntent.action = "ACTION_PAUSE_MUSIC"
        startService(pauseIntent)

        playIcon.visibility = View.VISIBLE
        pauseIcon.visibility = View.GONE
        MusicService.playStatus = false

        updateSeekBar(MusicService.currentPosition, MusicService.musicDuration)
    }

    private fun resumeMusic() {
        if(MusicService.stopping){
            MusicService.stopping = false
            audioUrl = Music.playList[MusicService.playlistID].src[MusicService.indexMusic]
            PlayMusicService(MusicService.playlistID, MusicService.indexMusic, audioUrl)

            pauseIcon.visibility = View.VISIBLE
            playIcon.visibility = View.GONE
            MusicService.playStatus = true
        }else {
            val resumeIntent = Intent(this, MusicService::class.java)
            resumeIntent.action = "ACTION_RESUME_MUSIC"
            startService(resumeIntent)

            pauseIcon.visibility = View.VISIBLE
            playIcon.visibility = View.GONE
            MusicService.playStatus = true

            // Update seekbar progress dan startTime ketika activity di-resume
            updateSeekBar(MusicService.currentPosition, MusicService.musicDuration)
        }
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                // Kirim perintah ke MusicService untuk mengatur posisi audio musik
                val intent = Intent(this@PlayerActivity, MusicService::class.java)
                intent.action = "ACTION_SET_MUSIC_POSITION"
                intent.putExtra("POSITION", progress)
                startService(intent)

                // Update startTime (durasi) sesuai dengan posisi audio musik yang baru
                startTime.text = formatTime(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // Tidak perlu diimplementasikan
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    private fun PlayNextSong() {
        ResetSeekBarProgress()
        currentIndex++
        if (currentIndex == MusicService.totalLagu) {
            currentIndex = 0
        }
        PlaySelectedSong(currentIndex)
        LoadLayout(playlistID, currentIndex)

        MusicService.playStatus = true
        PlayPauseIcon()
    }

    private fun PlayPreviousSong() {
        ResetSeekBarProgress()
        MusicService.mediaPlayer?.reset()
        MusicService.mediaPlayer?.stop()

        currentIndex--
        if (currentIndex < 0) {
            currentIndex = MusicService.totalLagu - 1
        }

        PlaySelectedSong(currentIndex)
        LoadLayout(playlistID, currentIndex)

        MusicService.playStatus = true
        PlayPauseIcon()

    }

    private fun PlaySelectedSong(index: Int) {
        // Stop and reset MediaPlayer
        ResetSeekBarProgress()
        MusicService.mediaPlayer?.reset()
        MusicService.mediaPlayer?.stop()

        // Start new song
        audioUrl = Music.playList[playlistID].src[index]
        PlayMusicService(playlistID, index, audioUrl)
        audioTitle = Music.playList[playlistID].title[index]

        MusicService.playStatus = true
        PlayPauseIcon()
    }

    private fun ResetSeekBarProgress() {
        seekBar.progress = 0
        startTime.text = formatTime(0)
        endTime.text = "..."
    }

    override fun onResume() {
        super.onResume()
        // Daftarkan BroadcastReceiver ketika activity di-resume
        val intentFilter = IntentFilter("ACTION_PLAY_NEXT")
        registerReceiver(playNextReceiver, intentFilter)

        val intentStopFilter = IntentFilter("ACTION_STOP_MUSIC")
    }

    override fun onPause() {
        super.onPause()
        // Hapus pendaftaran BroadcastReceiver ketika activity di-pause
        unregisterReceiver(playNextReceiver)
    }

    override fun onBackPressed() {
        val intent = Intent(this, PlaylistActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun stopMusicService() {
        // Hentikan layanan MusicService
        val stopIntent = Intent(this, MusicService::class.java)
        stopIntent.action = "ACTION_STOP_MUSIC"
        stopService(stopIntent)
        ResetSeekBarProgress()

        playIcon.visibility = View.VISIBLE
        pauseIcon.visibility = View.GONE
        MusicService.playStatus = false
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
}