package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object SoundSystem {
    private val scope = CoroutineScope(Dispatchers.Default)
    private const val SAMPLE_RATE = 44100

    /**
     * Play procedural synthesized sound effect
     */
    fun playAriseSound() {
        scope.launch {
            // Iconic "ARISE" sound: Sub-bass swell + shimmering high harmonic resonance
            val durationMs = 1200
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples

                // Frequency sweeps from 65Hz up to 220Hz with a dark chord
                val freqBass = 65.0 + (1 - progress) * 30.0
                val freqHarmonic1 = 130.0 + progress * 80.0
                val freqChime = 520.0 + sin(progress * PI * 4) * 40.0

                val envelope = when {
                    progress < 0.1 -> progress / 0.1
                    progress > 0.7 -> (1.0 - progress) / 0.3
                    else -> 1.0
                }

                val sampleBass = sin(2 * PI * freqBass * t) * 0.5
                val sampleHarmonic = sin(2 * PI * freqHarmonic1 * t) * 0.3
                val sampleChime = sin(2 * PI * freqChime * t) * 0.2

                val mixed = (sampleBass + sampleHarmonic + sampleChime) * envelope
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    fun playSwordSlash() {
        scope.launch {
            // Metallic sharp air-cutting slice
            val durationMs = 250
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 1600.0 * (1.0 - progress * 0.8) + 400.0
                val noise = (Math.random() * 2.0 - 1.0) * 0.4
                val tone = sin(2 * PI * freq * t) * 0.6
                val envelope = exp(-progress * 8.0)

                val mixed = (tone + noise) * envelope
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    fun playSystemBell() {
        scope.launch {
            // Level Up / System quest complete bell
            val durationMs = 800
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val f1 = sin(2 * PI * 880.0 * t) * 0.4
                val f2 = sin(2 * PI * 1320.0 * t) * 0.3
                val f3 = sin(2 * PI * 1760.0 * t) * 0.3
                val envelope = exp(-progress * 4.0)

                val mixed = (f1 + f2 + f3) * envelope
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    fun playPenaltyAlarm() {
        scope.launch {
            // Crimson Penalty Zone Warning Pulse
            val durationMs = 600
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val pulse = (sin(2 * PI * 6.0 * t) > 0)
                val freq = if (pulse) 440.0 else 330.0
                val tone = sin(2 * PI * freq * t) * 0.7

                buffer[i] = (tone * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    fun playRulersAuthority() {
        scope.launch {
            // Telekinetic shockwave thump
            val durationMs = 700
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 120.0 * (1.0 - progress * 0.7) + 35.0
                val tone = sin(2 * PI * freq * t) * 0.8
                val envelope = (1.0 - progress) * (1.0 - progress)

                val mixed = tone * envelope
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    fun playMonarchDomainHum() {
        scope.launch {
            // Shadow domain expansion ambient resonance
            val durationMs = 1500
            val numSamples = (SAMPLE_RATE * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val f1 = sin(2 * PI * 75.0 * t) * 0.4
                val f2 = sin(2 * PI * 150.0 * t + sin(2 * PI * 3.0 * t)) * 0.3
                val f3 = sin(2 * PI * 300.0 * t) * 0.2
                val envelope = sin(progress * PI)

                val mixed = (f1 + f2 + f3) * envelope
                buffer[i] = (mixed * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    private fun playBuffer(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()

            // Release after playing
            Thread.sleep((buffer.size * 1000L / SAMPLE_RATE) + 100)
            audioTrack.stop()
            audioTrack.release()
        } catch (_: Exception) {
            // Ignore audio device access exceptions on headless environments
        }
    }
}
