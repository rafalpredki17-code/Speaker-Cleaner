package com.example

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin

class AudioPlayer {
    private var audioTrack: AudioTrack? = null
    private var job: Job? = null
    private val sampleRate = 44100
    suspend fun startPlaying(frequency: Double = 165.0) = withContext(Dispatchers.IO) {
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
            return@withContext
        }

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.play()

        val buffer = ShortArray(bufferSize)
        var angle = 0.0
        val angleIncrement = 2 * PI * frequency / sampleRate

        job = launch {
            while (isActive) {
                for (i in buffer.indices) {
                    buffer[i] = (sin(angle) * Short.MAX_VALUE).toInt().toShort()
                    angle += angleIncrement
                }
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    fun stopPlaying() {
        job?.cancel()
        job = null
        try {
            audioTrack?.stop()
        } catch (e: Exception) {
            // Ignore
        }
        audioTrack?.release()
        audioTrack = null
    }
}
