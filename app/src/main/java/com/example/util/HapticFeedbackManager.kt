package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticFeedbackManager {

    private fun getVibrator(context: Context): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator ?: (context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Tactile feedback for successful authentication (unlocking vault).
     * Crisp, affirming two-beat pulse.
     */
    fun performSuccessFeedback(context: Context) {
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Rising two-pulse affirmation: 30ms light, 45ms pause, 55ms strong
                val timings = longArrayOf(0, 35, 45, 60)
                val amplitudes = intArrayOf(0, 160, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 35, 45, 60), -1)
            }
        } catch (e: Exception) {
            // Fallback for devices restricting custom amplitudes
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(60)
                }
            } catch (ignored: Exception) {}
        }
    }

    /**
     * Tactile feedback for failed authentication (incorrect PIN/Password/Biometric).
     * Distinctive triple error rejection vibration buzz.
     */
    fun performFailureFeedback(context: Context) {
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Strong triple-buzz rejection pattern
                val timings = longArrayOf(0, 70, 50, 70, 50, 90)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 70, 50, 70, 50, 90), -1)
            }
        } catch (e: Exception) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(180, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(180)
                }
            } catch (ignored: Exception) {}
        }
    }

    /**
     * Subtle tactile tick for keypad digit entry.
     */
    fun performKeypressFeedback(context: Context) {
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(12, 100))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(12)
            }
        } catch (ignored: Exception) {}
    }

    /**
     * Heavy warning vibration when excessive failed attempts trigger temporary lockout.
     */
    fun performLockoutFeedback(context: Context) {
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 150, 80, 250)
                val amplitudes = intArrayOf(0, 255, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 150, 80, 250), -1)
            }
        } catch (ignored: Exception) {}
    }
}
