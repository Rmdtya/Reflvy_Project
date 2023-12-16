package com.crysure.reflvy.utils

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.crysure.reflvy.PlayerActivity
import com.crysure.reflvy.R
import com.crysure.reflvy.data.Music

class MusicService : Service() {

    private lateinit var handler: Handler
    private lateinit var updateSeekBarRunnable: Runnable
    private var lastPlaybackPosition: Int = 0
    private lateinit var sharedPreferencesService : SharedPreferences

    private var notificationManager: NotificationManager? = null


    companion object {
        var audioTitle: String = ""
        var mediaPlayer: MediaPlayer? = null
        var playlistID : Int = 0
        var indexMusic : Int = 0
        var musicDuration : Int = 0
        var currentPosition : Int = 0
        var playStatus : Boolean = false
        var CHANNEL_ID = "MyMusicChannel"
        var isWaiting = false
        var totalLagu : Int = 0
        private const val NOTIFICATION_ID = 2
        var stopping : Boolean = false

        fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MusicService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                "ACTION_PAUSE_MUSIC" -> pauseMusic()
                "ACTION_RESUME_MUSIC" -> resumeMusic()
                "SEND_PLAY_STATUS" -> SendPlayStatus()
                "ACTION_SET_MUSIC_POSITION" -> {
                    val position = intent.getIntExtra("POSITION", 0)
                    setMusicPosition(position)
                }
                "ACTION_STOP_MUSIC" -> stopMusic() // Tangani tindakan stop
                else -> {
                    val audioUrl = intent.getStringExtra("AUDIO_URL") ?: ""
                    playlistID = intent.getIntExtra("ID_PLAYLIST", 0)
                    indexMusic = intent.getIntExtra("INDEX_AUDIO", 0)
                    playMusic(audioUrl)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun playMusic(audioUrl: String) {
        totalLagu = Music.playList[playlistID].title.size
        audioTitle = Music.playList[playlistID].title[indexMusic]
        mediaPlayer = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        mediaPlayer?.setAudioAttributes(audioAttributes)

        mediaPlayer?.setDataSource(audioUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            musicDuration = mediaPlayer?.duration ?: 0
            sendSeekBarUpdateBroadcast(currentPosition = 0, musicDuration)
            it.start()

            CreateNotification()
        }
        //Toast.makeText(this, indexMusic.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferencesService = getSharedPreferences("SERVICE", Context.MODE_PRIVATE)

        val editor = sharedPreferencesService.edit()
        editor.putBoolean("musicStart", true)
        editor.apply()

//        val sharedPreferences = getSharedPreferences("LOAD_LAYOUT", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("isLoaded", true)
//        editor.apply()

        val isMusicServiceActive = isServiceRunning(this)

        if (isMusicServiceActive) {
            handler = Handler()
            updateSeekBarRunnable = object : Runnable {
                override fun run() {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            currentPosition = it.currentPosition
                            sendSeekBarUpdateBroadcast(currentPosition, musicDuration)

                            if (currentPosition >= musicDuration - 1000) {
                                isWaiting = true
                                WaitingNextSong()
                            }
                        }
                    }
                    handler.postDelayed(this, 1000)
                }
            }
            handler.postDelayed(updateSeekBarRunnable, 0)
        }
    }

    private fun sendSeekBarUpdateBroadcast(currentPosition: Int, duration: Int) {
        val intent = Intent("ACTION_UPDATE_SEEK_BAR")
        intent.putExtra("CURRENT_POSITION", currentPosition)
        intent.putExtra("AUDIO_DURATION", duration)
        sendBroadcast(intent)
    }

    private fun pauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                lastPlaybackPosition = it.currentPosition
                it.pause()
                playStatus = false
            }
        }
        SendPlayStatus()
        CreateNotification()
    }

    private fun resumeMusic() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.seekTo(lastPlaybackPosition)
                it.start()
                playStatus = true
                sendSeekBarUpdateBroadcast(lastPlaybackPosition, musicDuration)
            }
        }
        SendPlayStatus()
        CreateNotification()
    }

    private fun SendPlayStatus(){
        val intentStatus = Intent("PLAY_STATUS")
        sendBroadcast(intentStatus)
    }

    fun setMusicPosition(position: Int) {
        mediaPlayer?.let {
            if (playStatus) {
                it.seekTo(position)
                currentPosition = position
            } else {
                lastPlaybackPosition = position
            }
        }
    }

    private fun CreateNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Tambahkan tindakan play/pause pada notifikasi
        var playPauseAction = if (playStatus) "Pause" else "Play"
        val playPauseIntent = Intent(this, MusicService::class.java)
        playPauseIntent.action = if (playStatus) "ACTION_PAUSE_MUSIC" else "ACTION_RESUME_MUSIC"
        //Toast.makeText(this, playStatus.toString(), Toast.LENGTH_SHORT).show()

        val playPausePendingIntent = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val playPauseActionIcon =
            if (playStatus) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24

        // Tambahkan tindakan stop pada notifikasi
        val stopIntent = Intent(this, MusicService::class.java)
        stopIntent.action = "ACTION_STOP_MUSIC"
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val stopActionIcon = R.drawable.baseline_stop_24

        // Tambahkan notifikasi yang akan ditampilkan saat foreground service berjalan


        var musicShared = sharedPreferencesService.getBoolean("musicStart", true)

        if(musicShared){
            val notificationIntent =
                PlayerActivity.getPlayerActivityPendingIntent(this, playlistID, indexMusic)
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Service")
                .setContentText("Playing music : " + Music.playList[playlistID].title[indexMusic])
                .setSmallIcon(R.drawable.reflvy_logo)
                .setContentIntent(notificationIntent)
                .setSound(null)
                .setSilent(true)
                .addAction(playPauseActionIcon, playPauseAction, playPausePendingIntent)
                .addAction(stopActionIcon, "Stop", stopPendingIntent) // Tambahkan tindakan stop
                .build()

            // Mulai foreground service dengan notifikasi yang dibuat di atas
            startForeground(NOTIFICATION_ID, notificationBuilder)
        }

    }

    private fun WaitingNextSong() {
        isWaiting = true
        // Lakukan proses penundaan

        Handler().postDelayed({
            if(isWaiting){
                indexMusic++
                if (indexMusic >= totalLagu) {
                    indexMusic = 0
                }
                val audioUrl = Music.playList[playlistID].src[indexMusic]
                playMusic(audioUrl)

                // Kirim broadcast ke PlayerActivity dengan membawa index lagu yang baru
                val intent = Intent("ACTION_PLAY_NEXT")
                intent.putExtra("PLAYLIST", playlistID)
                intent.putExtra("INDEX", indexMusic)
                sendBroadcast(intent)
            }
            isWaiting = false
            // Tambahkan kode yang ingin dieksekusi setelah penundaan selesai, misalnya:
        }, 3000)
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null

        stopping = true

        //Toast.makeText(this, "Stopping", Toast.LENGTH_SHORT).show()
        val intentStop = Intent("ACTION_STOP_MUSIC")
        sendBroadcast(intentStop)
        // Hentikan layanan dan musik ketika tindakan stop ditekan pada notifikasi
        stopForeground(true)
        stopSelf()

        val editor = sharedPreferencesService.edit()
        editor.putBoolean("musicStart", false)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

