package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AutoLockDuration
import com.example.data.local.ProtectedAppEntity
import com.example.data.local.SecurityLogEntity
import com.example.data.local.SecurityStorage
import com.example.data.local.VaultDatabase
import com.example.data.model.InstalledAppInfo
import com.example.data.repository.AppDiscoveryRepository
import com.example.data.repository.VaultRepository
import com.example.util.HapticFeedbackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthMode {
    PIN,
    PASSWORD
}

class VaultViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VaultDatabase.getInstance(application)
    val securityStorage = SecurityStorage(application)
    val repository = VaultRepository(db.protectedAppDao(), db.securityLogDao(), securityStorage)
    private val discoveryRepo = AppDiscoveryRepository(application)

    val protectedApps: StateFlow<List<ProtectedAppEntity>> = repository.allProtectedApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val protectedCount: StateFlow<Int> = repository.protectedCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val securityLogs: StateFlow<List<SecurityLogEntity>> = repository.recentLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    private val _isVaultSetup = MutableStateFlow(securityStorage.isVaultSetup())
    val isVaultSetup: StateFlow<Boolean> = _isVaultSetup.asStateFlow()

    private val _authMode = MutableStateFlow(AuthMode.PIN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    private val _pinInput = MutableStateFlow("")
    val pinInput: StateFlow<String> = _pinInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _deviceApps = MutableStateFlow<List<InstalledAppInfo>>(emptyList())
    val deviceApps: StateFlow<List<InstalledAppInfo>> = _deviceApps.asStateFlow()

    private val _selectedPackages = MutableStateFlow<Set<String>>(emptySet())
    val selectedPackages: StateFlow<Set<String>> = _selectedPackages.asStateFlow()

    private val _appSearchQuery = MutableStateFlow("")
    val appSearchQuery: StateFlow<String> = _appSearchQuery.asStateFlow()

    private val _appCategoryFilter = MutableStateFlow("All")
    val appCategoryFilter: StateFlow<String> = _appCategoryFilter.asStateFlow()

    private val _isLoadingApps = MutableStateFlow(false)
    val isLoadingApps: StateFlow<Boolean> = _isLoadingApps.asStateFlow()

    // Settings States
    private val _biometricEnabled = MutableStateFlow(securityStorage.isBiometricEnabled())
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    private val _autoLockDuration = MutableStateFlow(securityStorage.getAutoLockDuration())
    val autoLockDuration: StateFlow<AutoLockDuration> = _autoLockDuration.asStateFlow()

    private val _lockOnScreenOff = MutableStateFlow(securityStorage.isLockOnScreenOff())
    val lockOnScreenOff: StateFlow<Boolean> = _lockOnScreenOff.asStateFlow()

    private val _lockOnLeaveForeground = MutableStateFlow(securityStorage.isLockOnLeaveForeground())
    val lockOnLeaveForeground: StateFlow<Boolean> = _lockOnLeaveForeground.asStateFlow()

    private val _securityNotifications = MutableStateFlow(securityStorage.isSecurityNotificationsEnabled())
    val securityNotifications: StateFlow<Boolean> = _securityNotifications.asStateFlow()

    private var lastBackgroundTimestamp = 0L

    init {
        // Load initial app discovery in background
        loadDeviceApps()
    }

    fun setAuthMode(mode: AuthMode) {
        _authMode.value = mode
        _authError.value = null
        _pinInput.value = ""
        _passwordInput.value = ""
    }

    fun onPinDigit(digit: String) {
        if (_pinInput.value.length < 4) {
            val newPin = _pinInput.value + digit
            _pinInput.value = newPin
            _authError.value = null
            HapticFeedbackManager.performKeypressFeedback(getApplication())

            if (newPin.length == 4) {
                verifyAndUnlockPin(newPin)
            }
        }
    }

    fun onPinDelete() {
        if (_pinInput.value.isNotEmpty()) {
            _pinInput.value = _pinInput.value.dropLast(1)
            _authError.value = null
            HapticFeedbackManager.performKeypressFeedback(getApplication())
        }
    }

    fun onPasswordChange(text: String) {
        _passwordInput.value = text
        _authError.value = null
    }

    fun verifyAndUnlockPassword() {
        if (securityStorage.isLockedOut()) {
            val secs = securityStorage.getRemainingLockoutSeconds()
            _authError.value = "Too many failed attempts. Try in $secs seconds."
            HapticFeedbackManager.performLockoutFeedback(getApplication())
            return
        }

        val isValid = securityStorage.verifyPassword(_passwordInput.value)
        if (isValid) {
            _isUnlocked.value = true
            _authError.value = null
            _passwordInput.value = ""
            HapticFeedbackManager.performSuccessFeedback(getApplication())
            viewModelScope.launch {
                repository.logSecurityEvent("VAULT_UNLOCKED", "Unlocked using master password", true)
            }
        } else {
            _authError.value = "Incorrect password. Please try again."
            HapticFeedbackManager.performFailureFeedback(getApplication())
            viewModelScope.launch {
                repository.logSecurityEvent("FAILED_ATTEMPT", "Failed password attempt", false)
            }
        }
    }

    private fun verifyAndUnlockPin(pin: String) {
        if (securityStorage.isLockedOut()) {
            val secs = securityStorage.getRemainingLockoutSeconds()
            _authError.value = "Too many failed attempts. Try in $secs seconds."
            _pinInput.value = ""
            HapticFeedbackManager.performLockoutFeedback(getApplication())
            return
        }

        val isValid = securityStorage.verifyPin(pin)
        if (isValid) {
            _isUnlocked.value = true
            _authError.value = null
            _pinInput.value = ""
            HapticFeedbackManager.performSuccessFeedback(getApplication())
            viewModelScope.launch {
                repository.logSecurityEvent("VAULT_UNLOCKED", "Unlocked using master PIN", true)
            }
        } else {
            _authError.value = "Incorrect PIN. Try again."
            _pinInput.value = ""
            HapticFeedbackManager.performFailureFeedback(getApplication())
            viewModelScope.launch {
                repository.logSecurityEvent("FAILED_ATTEMPT", "Failed PIN attempt", false)
            }
        }
    }

    fun onBiometricSuccess() {
        _isUnlocked.value = true
        _authError.value = null
        _pinInput.value = ""
        HapticFeedbackManager.performSuccessFeedback(getApplication())
        viewModelScope.launch {
            repository.logSecurityEvent("VAULT_UNLOCKED", "Unlocked using biometric authentication", true)
        }
    }

    fun onBiometricFailure() {
        HapticFeedbackManager.performFailureFeedback(getApplication())
        viewModelScope.launch {
            repository.logSecurityEvent("FAILED_ATTEMPT", "Biometric authentication failed", false)
        }
    }

    fun setupVault(pin: String, pwd: String, enableBio: Boolean) {
        securityStorage.setupVault(pin, pwd, enableBio)
        _isVaultSetup.value = true
        _biometricEnabled.value = enableBio
        _isUnlocked.value = true
        viewModelScope.launch {
            repository.logSecurityEvent("VAULT_INITIALIZED", "Master security vault initialized", true)
        }
    }

    fun lockVault() {
        _isUnlocked.value = false
        _pinInput.value = ""
        _passwordInput.value = ""
        _authError.value = null
    }

    fun onAppMovedToBackground() {
        lastBackgroundTimestamp = System.currentTimeMillis()
        if (lockOnLeaveForeground.value && autoLockDuration.value == AutoLockDuration.IMMEDIATE) {
            lockVault()
        }
    }

    fun onAppMovedToForeground() {
        val duration = autoLockDuration.value.millis
        if (lockOnLeaveForeground.value && duration > 0 && lastBackgroundTimestamp > 0) {
            val elapsed = System.currentTimeMillis() - lastBackgroundTimestamp
            if (elapsed >= duration) {
                lockVault()
            }
        }
    }

    fun onScreenOff() {
        if (lockOnScreenOff.value) {
            lockVault()
        }
    }

    fun loadDeviceApps() {
        viewModelScope.launch {
            _isLoadingApps.value = true
            val apps = discoveryRepo.getInstalledLauncherApps()
            _deviceApps.value = apps
            _isLoadingApps.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _appSearchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        _appCategoryFilter.value = category
    }

    fun toggleAppSelection(packageName: String) {
        val current = _selectedPackages.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        _selectedPackages.value = current
    }

    fun saveSelectedAppsToVault() {
        viewModelScope.launch {
            val toAdd = _deviceApps.value.filter { _selectedPackages.value.contains(it.packageName) }
            val entities = toAdd.map {
                ProtectedAppEntity(
                    packageName = it.packageName,
                    appName = it.appName,
                    category = it.category
                )
            }
            repository.protectApps(entities)
            _selectedPackages.value = emptySet()
        }
    }

    fun unprotectApp(packageName: String, appName: String) {
        viewModelScope.launch {
            repository.unprotectApp(packageName, appName)
        }
    }

    fun launchAppFromVault(packageName: String, appName: String): Boolean {
        viewModelScope.launch {
            repository.recordAppLaunch(packageName, appName)
        }
        return discoveryRepo.launchApp(packageName)
    }

    fun updateAutoLock(duration: AutoLockDuration) {
        securityStorage.setAutoLockDuration(duration)
        _autoLockDuration.value = duration
    }

    fun updateBiometric(enabled: Boolean) {
        securityStorage.setBiometricEnabled(enabled)
        _biometricEnabled.value = enabled
    }

    fun updateLockOnScreenOff(enabled: Boolean) {
        securityStorage.setLockOnScreenOff(enabled)
        _lockOnScreenOff.value = enabled
    }

    fun updateLockOnLeaveForeground(enabled: Boolean) {
        securityStorage.setLockOnLeaveForeground(enabled)
        _lockOnLeaveForeground.value = enabled
    }

    fun updateSecurityNotifications(enabled: Boolean) {
        securityStorage.setSecurityNotificationsEnabled(enabled)
        _securityNotifications.value = enabled
    }

    fun changePin(currentPin: String, newPin: String): Boolean {
        if (!securityStorage.verifyPin(currentPin)) {
            HapticFeedbackManager.performFailureFeedback(getApplication())
            return false
        }
        securityStorage.changePin(newPin)
        HapticFeedbackManager.performSuccessFeedback(getApplication())
        viewModelScope.launch {
            repository.logSecurityEvent("PIN_CHANGED", "Master PIN updated securely", true)
        }
        return true
    }

    fun changePassword(currentPwd: String, newPwd: String): Boolean {
        if (!securityStorage.verifyPassword(currentPwd)) {
            HapticFeedbackManager.performFailureFeedback(getApplication())
            return false
        }
        securityStorage.changePassword(newPwd)
        HapticFeedbackManager.performSuccessFeedback(getApplication())
        viewModelScope.launch {
            repository.logSecurityEvent("PASSWORD_CHANGED", "Master password updated securely", true)
        }
        return true
    }

    fun clearSecurityLogs() {
        viewModelScope.launch {
            repository.clearLogs()
            HapticFeedbackManager.performSuccessFeedback(getApplication())
        }
    }

    fun resetVault() {
        viewModelScope.launch {
            repository.resetVault()
            _isVaultSetup.value = false
            _isUnlocked.value = false
            _selectedPackages.value = emptySet()
        }
    }
}
