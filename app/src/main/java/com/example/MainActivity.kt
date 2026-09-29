package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.navigation.VaultNavGraph
import com.example.ui.theme.VaultHideTheme
import com.example.ui.viewmodel.VaultViewModel
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private val vaultViewModel: VaultViewModel by viewModels()

    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                vaultViewModel.onScreenOff()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Register screen off receiver for auto-lock
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)

        // Lifecycle observer for foreground / background auto-lock
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        vaultViewModel.onAppMovedToBackground()
                    }
                    Lifecycle.Event.ON_START -> {
                        vaultViewModel.onAppMovedToForeground()
                    }
                    else -> {}
                }
            }
        )

        setContent {
            VaultHideTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VaultNavGraph(
                        viewModel = vaultViewModel,
                        onBiometricPromptRequest = { showBiometricPrompt() }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(screenOffReceiver)
        } catch (e: Exception) {
            // Already unregistered
        }
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)
        val canAuth = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
        )

        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(
                this,
                "Biometric authentication is not configured or unavailable on this device. Please use PIN or Password.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val executor: Executor = ContextCompat.getMainExecutor(this)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock VaultHide")
            .setSubtitle("Private App Vault")
            .setDescription("Verify your fingerprint or face to access protected apps")
            .setNegativeButtonText("Use PIN/Password")
            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    vaultViewModel.onBiometricSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        vaultViewModel.onBiometricFailure()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    vaultViewModel.onBiometricFailure()
                    Toast.makeText(this@MainActivity, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}
