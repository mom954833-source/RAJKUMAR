package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

enum class AutoLockDuration(val label: String, val millis: Long) {
    IMMEDIATE("Immediately", 0L),
    SECONDS_30("After 30 seconds", 30_000L),
    MINUTE_1("After 1 minute", 60_000L),
    MINUTES_5("After 5 minutes", 300_000L);

    companion object {
        fun fromName(name: String): AutoLockDuration {
            return entries.find { it.name == name } ?: IMMEDIATE
        }
    }
}

class SecurityStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("vaulthide_security_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SALT = "vh_crypto_salt"
        private const val KEY_PIN_HASH = "vh_pin_hash"
        private const val KEY_PASSWORD_HASH = "vh_password_hash"
        private const val KEY_BIOMETRIC_ENABLED = "vh_biometric_enabled"
        private const val KEY_AUTO_LOCK = "vh_auto_lock_duration"
        private const val KEY_LOCK_SCREEN_OFF = "vh_lock_screen_off"
        private const val KEY_LOCK_LEAVE_FOREGROUND = "vh_lock_leave_foreground"
        private const val KEY_SECURITY_NOTIFS = "vh_security_notifs"
        private const val KEY_FAILED_ATTEMPTS = "vh_failed_attempts"
        private const val KEY_LOCKOUT_UNTIL = "vh_lockout_until"
        private const val KEY_VAULT_INITIALIZED = "vh_vault_initialized"
    }

    private fun getOrCreateSalt(): ByteArray {
        val existingSaltBase64 = prefs.getString(KEY_SALT, null)
        if (existingSaltBase64 != null) {
            return Base64.decode(existingSaltBase64, Base64.NO_WRAP)
        }
        val salt = ByteArray(32)
        SecureRandom().nextBytes(salt)
        val encoded = Base64.encodeToString(salt, Base64.NO_WRAP)
        prefs.edit().putString(KEY_SALT, encoded).apply()
        return salt
    }

    private fun hashWithSalt(input: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashed = md.digest(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hashed, Base64.NO_WRAP)
    }

    fun isVaultSetup(): Boolean {
        return prefs.getBoolean(KEY_VAULT_INITIALIZED, false) &&
                (prefs.getString(KEY_PIN_HASH, null) != null || prefs.getString(KEY_PASSWORD_HASH, null) != null)
    }

    fun setupVault(pin: String, password: String, enableBiometric: Boolean) {
        val salt = getOrCreateSalt()
        val pinHash = hashWithSalt(pin, salt)
        val pwdHash = hashWithSalt(password, salt)

        prefs.edit()
            .putString(KEY_PIN_HASH, pinHash)
            .putString(KEY_PASSWORD_HASH, pwdHash)
            .putBoolean(KEY_BIOMETRIC_ENABLED, enableBiometric)
            .putBoolean(KEY_VAULT_INITIALIZED, true)
            .putInt(KEY_FAILED_ATTEMPTS, 0)
            .putLong(KEY_LOCKOUT_UNTIL, 0L)
            .apply()
    }

    fun verifyPin(pin: String): Boolean {
        val storedHash = prefs.getString(KEY_PIN_HASH, null) ?: return false
        val salt = getOrCreateSalt()
        val computedHash = hashWithSalt(pin, salt)
        val matches = (storedHash == computedHash)
        handleAttemptResult(matches)
        return matches
    }

    fun verifyPassword(password: String): Boolean {
        val storedHash = prefs.getString(KEY_PASSWORD_HASH, null) ?: return false
        val salt = getOrCreateSalt()
        val computedHash = hashWithSalt(password, salt)
        val matches = (storedHash == computedHash)
        handleAttemptResult(matches)
        return matches
    }

    fun changePin(newPin: String) {
        val salt = getOrCreateSalt()
        val newHash = hashWithSalt(newPin, salt)
        prefs.edit().putString(KEY_PIN_HASH, newHash).apply()
    }

    fun changePassword(newPassword: String) {
        val salt = getOrCreateSalt()
        val newHash = hashWithSalt(newPassword, salt)
        prefs.edit().putString(KEY_PASSWORD_HASH, newHash).apply()
    }

    private fun handleAttemptResult(success: Boolean) {
        if (success) {
            prefs.edit()
                .putInt(KEY_FAILED_ATTEMPTS, 0)
                .putLong(KEY_LOCKOUT_UNTIL, 0L)
                .apply()
        } else {
            val failed = prefs.getInt(KEY_FAILED_ATTEMPTS, 0) + 1
            val editor = prefs.edit().putInt(KEY_FAILED_ATTEMPTS, failed)
            if (failed >= 5) {
                // 30 seconds cooldown after 5 failed attempts
                editor.putLong(KEY_LOCKOUT_UNTIL, System.currentTimeMillis() + 30_000L)
            }
            editor.apply()
        }
    }

    fun isLockedOut(): Boolean {
        val lockoutUntil = prefs.getLong(KEY_LOCKOUT_UNTIL, 0L)
        return System.currentTimeMillis() < lockoutUntil
    }

    fun getRemainingLockoutSeconds(): Int {
        val lockoutUntil = prefs.getLong(KEY_LOCKOUT_UNTIL, 0L)
        val diff = lockoutUntil - System.currentTimeMillis()
        return if (diff > 0) (diff / 1000).toInt() else 0
    }

    fun isBiometricEnabled(): Boolean {
        return prefs.getBoolean(KEY_BIOMETRIC_ENABLED, true)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
    }

    fun getAutoLockDuration(): AutoLockDuration {
        val name = prefs.getString(KEY_AUTO_LOCK, AutoLockDuration.IMMEDIATE.name)
        return AutoLockDuration.fromName(name ?: AutoLockDuration.IMMEDIATE.name)
    }

    fun setAutoLockDuration(duration: AutoLockDuration) {
        prefs.edit().putString(KEY_AUTO_LOCK, duration.name).apply()
    }

    fun isLockOnScreenOff(): Boolean {
        return prefs.getBoolean(KEY_LOCK_SCREEN_OFF, true)
    }

    fun setLockOnScreenOff(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LOCK_SCREEN_OFF, enabled).apply()
    }

    fun isLockOnLeaveForeground(): Boolean {
        return prefs.getBoolean(KEY_LOCK_LEAVE_FOREGROUND, true)
    }

    fun setLockOnLeaveForeground(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LOCK_LEAVE_FOREGROUND, enabled).apply()
    }

    fun isSecurityNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_SECURITY_NOTIFS, true)
    }

    fun setSecurityNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SECURITY_NOTIFS, enabled).apply()
    }

    fun resetAll() {
        prefs.edit().clear().apply()
    }
}
